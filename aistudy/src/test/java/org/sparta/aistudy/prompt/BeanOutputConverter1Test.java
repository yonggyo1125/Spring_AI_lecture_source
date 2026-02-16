package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sparta.aistudy.prompt.dto.University;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class BeanOutputConverter1Test {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void lowLevelTest() {
        // 1. 컨버터 및 프롬프트 템플릿 준비
        BeanOutputConverter<University> converter = new BeanOutputConverter<>(University.class);
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{city}의 대학교 이름 5개를 출력하세요. {format}")
                .build();

        // 2. 데이터 바인딩 및 프롬프트 생성
        Prompt prompt = promptTemplate.create(Map.of("city", "인천", "format", converter.getFormat()));

        // 3. 호출 및 결과 받기 (String 형태)
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        // 4. 저수준 변환: 문자열을 University로 변환
        University university = converter.convert(response);

        log.info("답변: {}", university);

    }
}
