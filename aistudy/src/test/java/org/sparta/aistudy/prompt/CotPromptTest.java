package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class CotPromptTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
        String answer = solveComplexProblem("A는 B보다 크고 B는 C보다 작아. 누가 제일 커?");
        log.info("답변: {}", answer);
    }

    public String solveComplexProblem(String problem) {
        return this.chatClient.prompt()
                .system("""
                        당신은 논리적인 분석가입니다. 
                        문제를 해결할 때 반드시 다음 단계를 준수하세요:
                        1. 주어진 정보를 분석합니다.
                        2. 단계별로 추론 과정을 나열합니다.
                        3. 최종 결론을 도출합니다.
                        """)
                .user(u -> u.text("""
                        문제: {problem}
                        
                        도움말: 한 걸음씩 차근차근 생각해 봅시다.
                        """)
                        .param("problem", problem))
                .call()
                .content();
    }
}
