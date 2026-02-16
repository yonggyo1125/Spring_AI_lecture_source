package org.sparta.aistudy.advisor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AdvisorEachTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
        String answer = chatClient.prompt()
                .advisors(
                        new LoggingAdvisor(),
                        new ValidationAdvisor(),
                        new MetricsAdvisor()
                )
                .user("스티브잡스의 명언 3개 알려줘")
                .call()
                .content();
        log.info("답변:\n{}", answer);
    }
}
