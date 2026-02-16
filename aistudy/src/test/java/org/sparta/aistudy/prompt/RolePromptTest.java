package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RolePromptTest {

    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
        String answer = getPersonalizedResponse("30년 경력의 프랑스 요리 쉐프", "크리스마스에 가족과 즐길 수 있는 요리 추천해줘");
        log.info("답변: {}", answer);
    }

    /**
     * 특정 역할을 동적으로 부여하여 답변을 생성합니다.
     */
    public String getPersonalizedResponse(String role, String userInput) {
        return this.chatClient.mutate()
                .build()
                .prompt()
                .system(sp -> sp.text("당신은 {role}입니다. 해당 역할의 전문성과 문체를 사용하여 답변하세요.")
                        .param("role", role)) // 동적 역할 부여
                .user(userInput)
                .call()
                .content();
    }
}
