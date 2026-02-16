package org.sparta.tools.datetime;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DateTimeService {
    private final ChatClient chatClient;

    private final DateTimeTools dateTimeTools;

    public DateTimeService(ChatClient.Builder builder, DateTimeTools dateTimeTools) {
        this.chatClient = builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
        this.dateTimeTools = dateTimeTools;
    }

    // LLM과 대화하는 메서드
    public String chat(String question) {
        return chatClient.prompt()
                .user(question)
                .tools(dateTimeTools)
                .call()
                .content();
    }
}
