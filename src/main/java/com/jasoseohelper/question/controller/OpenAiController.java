package com.jasoseohelper.question.controller;

import com.jasoseohelper.question.dto.AiRequestDTO;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/chat")
@RestController
public class OpenAiController {
    private final OpenAiChatModel openAiChatModel;

    public OpenAiController(OpenAiChatModel openAiChatModel) {
        this.openAiChatModel = openAiChatModel;
    }

    @PostMapping("/guide")
    public Map<String, String> getGuide(@RequestBody AiRequestDTO request) {
        String template = "Please give me a brief explanation in Korean of what I should focus on when answering this question. ";

        Map<String, String> responses = new HashMap<>();

        Prompt prompt = new Prompt((template + request.getTitle()));
        ChatResponse response = openAiChatModel.call(prompt);
        String openAiResponse = response.getResult().getOutput().getText();
        responses.put("guide", openAiResponse);

        return responses;
    }
    @PostMapping("/feedback")
    public Map<String, String> getFeedback(@RequestBody AiRequestDTO request) {
        String template = "질의 : 자기소개서 피드백 부탁 " +
                "1. 논리성, 흐름 (자연스럽지 않은 문장, 주장이 명확한지, 목표-경험 연결) " +
                "2. 구체성, 차별성 (지원자가 수동적인지, 경험 구체성, 차별성) " +
                "3.가독성&문법 체크\n";

        Map<String, String> responses = new HashMap<>();

        Prompt prompt = new Prompt((template +request.getTitle() + "\n" + request.getContent()));
        ChatResponse response = openAiChatModel.call(prompt);
        String openAiResponse = response.getResult().getOutput().getText();
        responses.put("feedback", openAiResponse);

        return responses;
    }
}