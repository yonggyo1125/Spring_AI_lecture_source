package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class TextClassificationTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
        String text = "오늘은 날씨가 매우 추워 외출하기 싫어. 그런데 비도 오네. 비가 오면 시원하고 좋은데, 좋은 건가 나쁜건가?";
        TextClassification classification = chatClient.prompt()
                // 1. 시스템 메시지: 분석 로직과 페르소나 부여 (1차 지침)
                .system("""
                        당신은 텍스트의 감정을 분석하는 전문가입니다.
                        입력된 문장의 뉘앙스를 살펴 [POSITIVE, NEUTRAL, NEGATIVE] 중 하나로 분류하세요.
                        - 단어뿐만 아니라 문맥의 흐름을 파악하십시오.
                        - 확실하지 않은 경우 NEUTRAL로 분류하십시오.
                        - 유효한 JSON으로 반환하십시오.
                        """)
                // 3. 사용자 메시지
                .user(u -> u.text("텍스트: {text}").param("text", text))
                .call()
                // 2. 출력 컨버터 설정 (BeanOutputConverter가 내부적으로 entity() 지침 생성)
                .entity(TextClassification.class);

        log.info("답변: {}", classification);

    }
}
