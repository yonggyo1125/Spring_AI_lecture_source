package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class StepBackPromptTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
        String answer = getStepBackAnswer("우리 집 냉장고 문이 잘 안 닫히는데 어떻게 고쳐야 해?");
        log.info("답변: {}", answer);
    }

    public String getStepBackAnswer(String originalQuestion) {

        // 1단계: Step-back 질문 생성
        String stepBackQuestion = chatClient.prompt()
                .user(u -> u.text("다음 질문에서 핵심적인 원리나 배경을 묻는 더 포괄적인 질문(Step-back question) 하나만 생성해줘. 질문: {question}")
                        .param("question", originalQuestion))
                .call()
                .content();

        log.info("stepBackQuestion: {}", stepBackQuestion);

        // 2단계: 배경 지식(Fact) 확보
        String backgroundKnowledge = chatClient.prompt()
                .user(stepBackQuestion)
                .call()
                .content();

        log.info("backgroundKnowledge: {}", backgroundKnowledge);

        // 3단계: 최종 답변 생성 (배경 지식 + 원래 질문)
        return chatClient.prompt()
                .system("너는 제공된 배경 지식을 바탕으로 사용자의 질문에 논리적으로 답변하는 전문가야.")
                .user(u -> u.text("""
                        [배경 지식]: {knowledge}
                        [사용자 질문]: {question}
                        
                        위 배경 지식을 참고하여 질문에 대해 구체적이고 정확하게 답변해줘.
                        """)
                        .param("knowledge", backgroundKnowledge)
                        .param("question", originalQuestion))
                .call()
                .content();
    }
}
