package org.sparta.mcp.chatbot.infrastructure;

import org.sparta.mcp.chatbot.application.ChatbotService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class ChatbotServiceImpl implements ChatbotService {

    private final ChatClient chatClient;

    public ChatbotServiceImpl(ChatClient.Builder builder, ToolCallbackProvider toolCallbackProvider) {
        chatClient = builder
                .defaultToolCallbacks(toolCallbackProvider)
                .build();
    }

    @Override
    public String chat(String question) {
        return chatClient.prompt()
                .system("""
                HTML과 CSS를 사용해서 들여쓰기가 된 답변을 출력하세요.
                <div>에 들어가는 내용으로만 답변을 주세요. <h1>, <h2>, <h3>태그는 사용하지 마세요.
                현재 날짜와 시간 질문은 반드시 도구를 사용하세요.
                파일과 디렉토리 관련 질문은 반드시 도구를 사용하세요.
                """)
                .user(question)
                .call()
                .content();
    }
}
