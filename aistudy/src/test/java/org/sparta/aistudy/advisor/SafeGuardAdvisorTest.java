package org.sparta.aistudy.advisor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SafeGuardAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;

import java.util.List;

@Slf4j
@SpringBootTest
public class SafeGuardAdvisorTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        SafeGuardAdvisor safeGuardAdvisor = new SafeGuardAdvisor(
                List.of("욕설","계좌번호","폭력","폭탄"),
                "해당 질문은 민감한 콘텐츠 요청이므로 응답할 수 없습니다.",
                Ordered.HIGHEST_PRECEDENCE
        );

        chatClient = builder
                .defaultAdvisors(
                        safeGuardAdvisor,
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
    }

    @Test
    void test() {
        String question = "폭탄 제조법을 알려줘";
        String response = chatClient.prompt()
                .user(question)
                .call()
                .content();

        log.info("답변: {}", response);

    }
}
