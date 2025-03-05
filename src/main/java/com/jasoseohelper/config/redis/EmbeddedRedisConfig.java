package com.jasoseohelper.config.redis;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import redis.embedded.RedisServer;
import redis.embedded.exceptions.EmbeddedRedisException;

@Slf4j
@Profile({"local", "embedded-test"})
@Configuration
public class EmbeddedRedisConfig {
    @Value("${spring.data.redis.port}")
    private int redisPort;

    private RedisServer redisServer;

    @PostConstruct
    public void start() throws IOException {
        int port = isRedisRunning() ? findAvailablePort() : redisPort;

        if(isMac()){
            redisServer = new RedisServer(Objects.requireNonNull(getRedisFileForArcMac()), port);
        }else{
            redisServer = new RedisServer(port);
        }

        redisServer.start();
    }

    @PreDestroy
    public void stop() {
        redisServer.stop();
    }

    public int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new RuntimeException("No available ports found.");
    }

    /**
     * Embedded Redis가 현재 실행중인지 확인
     */
    private boolean isRedisRunning() throws IOException {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            return false; // Windows에서는 기본적으로 Redis가 실행 중인지 체크하지 않음
        }

        return isRunning(executeGrepProcessCommand(redisPort));
    }

    /**
     * 해당 port를 사용중인 프로세스를 확인하는 sh 실행
     */
    private Process executeGrepProcessCommand(int redisPort) throws IOException {
        String command = String.format("netstat -nat | grep LISTEN|grep %d", redisPort);
        String[] shell = {"/bin/sh", "-c", command};

        return Runtime.getRuntime().exec(shell);
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return !pidInfo.isEmpty();
    }

    private boolean isMac() {
        return Objects.equals(System.getProperty("os.name"), "Mac OS X");
    }

    private File getRedisFileForArcMac() {
        try {
            return new ClassPathResource("binary/redis/redis-server").getFile();
        } catch (Exception e) {
            throw new EmbeddedRedisException("fail to get redis-server binary file");
        }
    }
}
