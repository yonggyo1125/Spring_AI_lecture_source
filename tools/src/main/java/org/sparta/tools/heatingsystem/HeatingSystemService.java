package org.sparta.tools.heatingsystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class HeatingSystemService {

    private final ChatClient chatClient;
    private final HeatingSystemTools heatingSystemTools;

    public HeatingSystemService(ChatClient.Builder builder, HeatingSystemTools heatingSystemTools) {
        this.chatClient = builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                ).build();
        this.heatingSystemTools = heatingSystemTools;
    }

    // LLM과 대화하는 메서드
    public String chat(String question) {
        return chatClient.prompt()
                .system("""
                    현재 온도가 사용자가 원하는 온도 이상이라면 난방 시스템을 중지하세요.
                    현재 온도가 사용자가 원하는 온도 이하라면 난방 시스템을 가동시켜주세요.
                 """)
                .user(question)
                .tools(heatingSystemTools)
                .toolContext(Map.of("controlKey", "heatingSystemKey2"))
                .call()
                .content();
    }
}
