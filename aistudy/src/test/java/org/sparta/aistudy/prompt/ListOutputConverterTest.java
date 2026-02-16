package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class ListOutputConverterTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void lowLevelTest() {
        // 1. 컨버터 초기화
        ListOutputConverter converter = new ListOutputConverter();

        // 2. 템플릿 작성 ({format} 플레이스홀더 포함)
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template("인천에서 유명한 관광지 목록 5개를 출력하세요. {format}")
                .build();

        // 3. 변수 바인딩 및 프롬프트 생성
        Prompt prompt = promptTemplate.create(Map.of("format", converter.getFormat()));

        // 4. 호출 및 결과 받기 (String 형태)
        String response = chatClient.prompt(prompt)
                .call()
                .content();

        // 5. 저수준 변환: 문자열을 List<String>으로 직접 변환
        List<String> locations = converter.convert(response);
        log.info("답변: {}", locations);
    }

}
