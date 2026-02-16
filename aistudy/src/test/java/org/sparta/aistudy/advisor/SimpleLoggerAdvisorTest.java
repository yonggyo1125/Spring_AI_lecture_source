package org.sparta.aistudy.advisor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;

@Slf4j
@SpringBootTest
public class SimpleLoggerAdvisorTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder
                .defaultAdvisors(
                        new MaxLengthAdvisor(Ordered.HIGHEST_PRECEDENCE),
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
    }

    @Test
    void test() {
        String question = "스티브잡스의 명언 3개를 알려줘";
        String response = chatClient.prompt()
               .advisors(advisorSpec -> advisorSpec.param("maxLength", 100))
               .user(question)
               .call()
               .content();

       log.info("답변:\n{}", response);
    }
}
