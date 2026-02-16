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

// 3. 세 번째 Advisor: 실행 시간 측정 시뮬레이션
@Slf4j
public class MetricsAdvisor implements CallAdvisor, StreamAdvisor {

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 3;
    }

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest request, CallAdvisorChain chain) {

        log.info("[Advisor 3] 실행 시간 측정 시작");
        ChatClientResponse response = chain.nextCall(request);
        log.info("[Advisor 3] 실행 시간 측정 종료");

        return response;
    }

    @Override
    public Flux<ChatClientResponse> adviseStream(ChatClientRequest request, StreamAdvisorChain chain) {

        log.info("[Advisor 3] 실행 시간 측정 시작");
        Flux<ChatClientResponse> response = chain.nextStream(request);
        log.info("[Advisor 3] 실행 시간 측정 종료");

        return response;
    }
}
