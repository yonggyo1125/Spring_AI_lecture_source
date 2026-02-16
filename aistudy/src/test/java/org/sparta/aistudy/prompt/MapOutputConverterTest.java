package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class MapOutputConverterTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void lowLevelTest() {
        // 1. 컨버터 및 프롬프트 템플릿 준비
        MapOutputConverter converter = new MapOutputConverter();
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("{university}대학교의 정보를 출력하세요. {format}")
                .build();

        // 2. 데이터 바인딩 및 프롬프트 생성
        Prompt prompt = promptTemplate.create(Map.of("university", "서울", "format", converter.getFormat()));

        // 3. 호출 및 결과 받기 (String 형태)
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        // 4. 저수준 변환: 문자열을 Map<String, Object>로 변환
        Map<String, Object> information = converter.convert(response);

        log.info("답변: {}", information);
    }
}
