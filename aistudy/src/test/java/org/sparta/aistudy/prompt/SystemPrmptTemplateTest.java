package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class SystemPrmptTemplateTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void useSystemPromptTest() {
        String message = "스프링 DI에 대해 설명해줘";

        SystemPromptTemplate promptTemplate = SystemPromptTemplate.builder()
                .template("당신은 이제부터 {role} 전문가입니다. 전문 용어를 섞어서 답변하세요.")
                .build();

        Prompt prompt = promptTemplate.create(Map.of("role", "스프링"));

        String answer = chatClient.prompt(prompt)
                .user(message)
                .call()
                .content();
        log.info("답변: {}", answer);

    }
}
