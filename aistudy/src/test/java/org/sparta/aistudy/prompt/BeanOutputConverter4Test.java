package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.aistudy.prompt.dto.University;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

@Slf4j
@SpringBootTest
public class BeanOutputConverter4Test {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void highLevelTest() {
        // 컨버터 준비 (List 형태 지정을 위해 ParameterizedTypeReference 사용)
        BeanOutputConverter<List<University>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});

        // LLM 호출 및 결과 받기
        String template = """
                    다음 도시들의 대학교 목록 5개를 출력하세요. 
                    도시: {cities}
                    """;
        List<University> universities = chatClient.prompt()
                .user(u -> u.text(template).param("cities", "서울,인천,부산"))
                .call()
                .entity(converter);

        log.info("답변:\n{}", universities);
    }
}
