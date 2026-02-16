package org.sparta.mcp.chatbot.application;

import reactor.core.publisher.Flux;

public interface ChatbotService {
    Flux<String> chat(String question);
}
