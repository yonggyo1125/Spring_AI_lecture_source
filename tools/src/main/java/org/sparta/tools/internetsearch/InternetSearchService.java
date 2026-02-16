package org.sparta.tools.internetsearch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InternetSearchService {

    private final ChatClient chatClient;

    private final InternetSearchTools internetSearchTools;

    public InternetSearchService(ChatClient.Builder builder, InternetSearchTools internetSearchTools) {
        chatClient = builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
        this.internetSearchTools = internetSearchTools;
    }

    public String chat(String question) {
        return chatClient.prompt()
                .system("""
                    HTML과 CSS를 사용해서 들여쓰기가 된 답변을 출력하세요.
                    <div>에 들어가는 내용으로만 답변을 주세요. <h1>, <h2>, <h3>태그는 사용하지 마세요.
                    """)
                .user(question)
                .tools(internetSearchTools)
                .call()
                .content();
    }
}
