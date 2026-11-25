
package com.SpringGrok.Grok.service;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class SkillExtractionService {

    private final ChatClient chatClient;

    public SkillExtractionService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public List<String> extractSkills(String text) {

        String prompt = """
                You are an expert technical skill extractor.

                Extract all relevant technical skills from
                the following text.

                TEXT:
                %s

                RULES:
                1. Extract only skills explicitly mentioned.
                2. Do not invent skills.
                3. Remove duplicate skills.
                4. Return only a valid JSON array of strings.
                5. Do not include explanations or Markdown.

                Example:
                ["Java", "Spring Boot", "MySQL", "React"]
                """.formatted(text);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(
                    org.springframework.core.ParameterizedTypeReference
                        .forType(List.class)
                );
    }
}
