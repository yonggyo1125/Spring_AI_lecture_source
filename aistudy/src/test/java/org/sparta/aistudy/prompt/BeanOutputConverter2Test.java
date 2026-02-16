package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.aistudy.prompt.dto.University;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class BeanOutputConverter2Test {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void highLevelTest() {
        // 컨버터 준비
        BeanOutputConverter<University> converter = new BeanOutputConverter<>(University.class);

        // LLM 호출 및 결과 받기
        University university = chatClient
                .prompt()
                .user("%s의 대학교 이름 5개를 출력하세요.".formatted("인천"))
                .call()
                .entity(University.class);

        log.info("답변: {}", university);
    }
}
