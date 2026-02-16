package org.sparta.tools.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Slf4j
@Service("recommendMovieService2")
public class RecommendMovieService {

    private final ChatClient chatClient;
    private final RecommendMovieTools recommendMovieTools;

    public RecommendMovieService(ChatClient.Builder builder, RecommendMovieTools recommendMovieTools) {
        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1))
                .build();
        this.recommendMovieTools = recommendMovieTools;
    }

    // LLM과 대화하는 메서드
    public String chat(String question) {
        return chatClient.prompt()
                .user("""
                    질문에 대해 답변해주세요.
                    사용자 ID가 존재하지 않을 경우, 진행을 멈추고, 
                    '[LLM] 질문을 처리할 수 없습니다.'라고 답변을 해주세요.
                    
                    질문: %s
                 """.formatted(question))
                .tools(recommendMovieTools)
                .call()
                .content();
    }
}
