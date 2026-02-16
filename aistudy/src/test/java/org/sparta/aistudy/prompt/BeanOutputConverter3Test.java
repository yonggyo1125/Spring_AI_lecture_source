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
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class BeanOutputConverter3Test {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void lowLevelTest() {
        // 1. 컨버터 및 프롬프트 템플릿 준비 (List 형태 지정을 위해 ParameterizedTypeReference 사용)
        BeanOutputConverter<List<University>> converter = new BeanOutputConverter<>(new ParameterizedTypeReference<>() {});
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("""
                    다음 도시들의 대학교 목록 5개를 출력하세요. 
                    도시: {cities}
                    {format}
                    """)
                .build();

        // 2. 데이터 바인딩 및 프롬프트 생성
        Prompt prompt = promptTemplate.create(Map.of("cities", "서울,인천,부산", "format", converter.getFormat()));

        // 3. 호출 및 결과 받기 (String 형태)
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        // 4. 저수준 변환: 문자열을 List<University>로 변환
        List<University> universities = converter.convert(response);

        log.info("답변:\n{}", universities);
    }
}
