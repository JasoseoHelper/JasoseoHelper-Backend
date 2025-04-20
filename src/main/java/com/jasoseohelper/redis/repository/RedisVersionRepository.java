package com.jasoseohelper.redis.repository;

import com.jasoseohelper.redis.entity.RedisTempVersion;
import org.springframework.data.repository.CrudRepository;

public interface RedisVersionRepository extends CrudRepository<RedisTempVersion, String> {
}
