package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class MapOutputConverter2Test {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void highLevelTest() {
        String template = "{university}대학교의 정보를 출력하세요.";

        Map<String, Object> information = chatClient.prompt()
                .user(u -> u.text(template).param("university", "서울"))
                .call()
                .entity(new MapOutputConverter());

        log.info("답변: {}", information);
    }
}
