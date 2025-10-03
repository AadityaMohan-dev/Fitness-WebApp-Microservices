package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service

public class GeminiService {

    private final WebClient webClient;
    @Value("${GEMINI_API_KEY}")
    private String geminiApiKey;
    @Value("${GEMINI_API_URL}")
    private String geminiApiUrl;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question){
        Map<String, Object> requestBody = Map.of(
                "content" , new Object[]{
                        Map.of("parts" , new Object[]{
                                Map.of("text", question)
                        })
                }
        );
        String response = webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
                return response;
    }
}
