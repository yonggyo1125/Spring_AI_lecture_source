package org.sparta.aistudy.chatbot2.application;

import reactor.core.publisher.Flux;

public interface LittlePrinceChatBotService {
    String generate(String question);
    Flux<String> generateStream(String question);
}