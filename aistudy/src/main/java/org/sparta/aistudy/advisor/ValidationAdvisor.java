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

// 2. 두 번째 Advisor: 데이터 검증 시뮬레이션
@Slf4j
public class ValidationAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 2;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        log.info("[Advisor 2] 데이터 유효성 체크...");
        ChatClientResponse response = chain.nextCall(request);
        log.info("[Advisor 2] 응답 데이터 검증 완료.");

        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {

        log.info("[Advisor 2] 데이터 유효성 체크...");
        Flux<ChatClientResponse> response = chain.nextStream(request);
        log.info("[Advisor 2] 응답 데이터 검증 완료.");

        return response;
    }
}
