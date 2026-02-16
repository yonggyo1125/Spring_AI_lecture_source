package org.sparta.aistudy.advisor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAdvisorChain;
import org.springframework.core.Ordered;
import reactor.core.publisher.Flux;

// 1. 첫 번째 Advisor: 요청 시작 로그
@Slf4j
public class LoggingAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    // Spring MVC Web을 사용하는 경우
    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        log.info("[Advisor 1] 요청 전처리 중...");
        ChatClientResponse response = chain.nextCall(request);
        log.info("[Advisor 1] 응답 후처리 중...");

        return response;
    }

    // Spring Reactive Web을 사용하는 경우
    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {

        log.info("[Advisor 1] 요청 전처리 중...");
        Flux<ChatClientResponse> response = chain.nextStream(request);
        log.info("[Advisor 1] 응답 후처리 중...");

        return response;
    }
}
