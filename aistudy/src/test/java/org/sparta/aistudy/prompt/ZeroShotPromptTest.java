package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class ZeroShotPromptTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder
                .defaultOptions(ChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.0) // 감정 분류 문구만 응답하므로 응답 다양성은 없습니다.
                        .maxTokens(4) // 범주화돤 감정 분류 내에서 답변하므로 4개의 토큰 이상을 넘지 않습니다.
                        .build())
                .build();
    }

    @Test
    void test() {
        String text = "오늘은 영하 20도로 피부가 아플 정도로 추워";
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("""
                    다음 텍스트의 감정을 [긍정, 부정, 중립] 중 하나로 분류하세요.
                    레이블만 반환하세요.
                    텍스트: {input}
                    """)
                .build();
        String sentiment = chatClient.prompt()
                .user(promptTemplate.render(Map.of("input", text)))
                .call()
                .content();

        log.info("분류: {}", sentiment);
    }

}
