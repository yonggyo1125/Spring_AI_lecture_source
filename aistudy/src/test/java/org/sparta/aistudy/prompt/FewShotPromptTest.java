package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class FewShotPromptTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.defaultOptions(
                    ChatOptions.builder()
                            .model("gpt-4o-mini")
                            .temperature(0.0)
                            .maxTokens(100)
                            .build()
                )
                .build();
    }

    @Test
    void test() {
        String reviewText = "이 상품은 색상도 맘에 들고 기능도 좋아요. 정말 맘에 듭니다.";
        String answer = chatClient.prompt()
                .messages(
                        // 1. 지침 설정 (System Message)
                        new SystemMessage("당신은 리뷰 감정 분석가입니다. 응답은 반드시 JSON 형식으로 'sentiment'와 'confidence'를 포함해야 합니다. "),

                        // 2. 예시 1 (User -> Assistant)
                        new UserMessage("이 제품 정말 최고예요! 배송도 빨랐습니다."),
                        new AssistantMessage("{\"sentiment\": \"positive\", \"confidence\": 0.98}"),

                        // 3. 예시 2 (User -> Assistant)
                        new UserMessage("생각보다 별로네요. 디자인은 예쁜데 품질이 떨어집니다."),
                        new AssistantMessage("{\"sentiment\": \"negative\", \"confidence\": 0.85}"),

                        // 4. 실제 사용자 입력
                        new UserMessage(reviewText)
                )
                .call()
                .content();
        log.info("답변: {}", answer);
    }
}
