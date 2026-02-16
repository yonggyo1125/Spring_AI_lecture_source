package org.sparta.aistudy.prompt;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@Slf4j
@SpringBootTest
public class PromptTemplateTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void usePromptTemplateTest() {
        // 1. 템플릿 정의
        String templateString = "{person}의 명언 3개를 한국어로 출력해줘";
        // PromptTemplate promptTemplate = new PromptTemplate(templateString);
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(templateString)
                .build();

        // 2. 데이터 바인딩 및 렌더링 확인
        String renderedContent = promptTemplate.render(Map.of("person", "스티브잡스"));
        log.info("생성된 프롬프트: {}", renderedContent);

        // 3. ChatClient를 통한 호출
        String answer = chatClient.prompt(renderedContent)
                .call()
                .content();

        log.info("생성된 응답: {}", answer);
    }

    @Test
    void usePromptTemplate2Test() {
        // 1. 템플릿 정의
        String templateString = "{person}의 명언 3개를 한국어로 출력해줘";
        // PromptTemplate promptTemplate = new PromptTemplate(templateString);
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(templateString)
                .build();

        // 2. 데이터 바인딩 및 렌더링 확인
        Prompt prompt = promptTemplate.create(Map.of("person", "스티브잡스"));
        // 3. ChatClient를 통한 호출
        String answer = chatClient.prompt(prompt)
                .call()
                .content();

        log.info("생성된 응답: {}", answer);
    }

    @Test
    void usePromptTemplate3Test() {
        // 1. 템플릿 정의
        String templateString = "{person}의 명언 3개를 한국어로 출력해줘";
        // PromptTemplate promptTemplate = new PromptTemplate(templateString);
        PromptTemplate promptTemplate = PromptTemplate.builder()
                .template(templateString)
                .build();

        // 2. 데이터 바인딩 및 렌더링 확인
        String renderedContent = promptTemplate.render();
        // 3. ChatClient를 통한 호출
        String answer = chatClient.prompt()
                .user(u -> u.text(renderedContent).param("person", "스티브잡스"))
                .call()
                .content();

        log.info("생성된 응답: {}", answer);
    }
}
