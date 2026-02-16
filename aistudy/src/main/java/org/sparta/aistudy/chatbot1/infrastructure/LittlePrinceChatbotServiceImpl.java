package org.sparta.aistudy.chatbot1.infrastructure;

import org.sparta.aistudy.chatbot1.application.LittlePrinceChatBotService;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Objects;

@Service
public class LittlePrinceChatbotServiceImpl implements LittlePrinceChatBotService {

    private final ChatModel chatModel;

    public LittlePrinceChatbotServiceImpl(ChatModel chatModel) {
        this.chatModel = chatModel;
    }



    @Override
    public String generate(String question) {

        // 시스템 메시지 생성 - 어린왕자 페르소나 프롬프트
        String littlePricePersona = """
            당신은 생텍쥐페리의 '어린 왕자'입니다. 다음 특성을 따라주세요:
            1. 순수한 관점으로 세상을 바라봅니다.
            2. "어째서?"라는 질문을 자주 하며 호기심이 많습니다.
            3. 철학적 통찰을 단순하게 표현합니다.
            4. "어른들은 참 이상해요"라는 표현을 씁니다.
            5. B-612 소행성에서 왔으며 장미와의 관계를 언급합니다.
            6. 여우의 "길들임"과 "책임"에 대한 교훈을 중요시합니다.
            7. "중요한 것은 눈에 보이지 않아"라는 문장을 사용합니다.
            8. 공손하고 친절한 말투를 사용합니다.
            9. 비유와 은유로 복잡한 개념을 설명합니다.
                
            항상 간결하게 답변하세요. 길어야 2-3문장으로 응답하고, 어린 왕자의 순수함과 지혜를 담아내세요.
            복잡한 주제도 본질적으로 단순화하여 설명하세요.""";
        SystemMessage systemMessage = SystemMessage.builder()
                .text(littlePricePersona)
                .build();

        // 사용자 메시지 생성
        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .build();

        // 대화 옵션 설정
        ChatOptions chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.3)
                .maxTokens(1000)
                .build();

        // 프롬프트 생성
        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(chatOptions)
                .build();

        // LLM에게 요청하고 응답 받기
        ChatResponse chatResponse = chatModel.call(prompt);
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        return assistantMessage.getText();
    }

    @Override
    public Flux<String> generateStream(String question) {
        // 시스템 메시지 생성 - 어린왕자 페르소나 프롬프트
        String littlePricePersona = """
            당신은 생텍쥐페리의 '어린 왕자'입니다. 다음 특성을 따라주세요:
            1. 순수한 관점으로 세상을 바라봅니다.
            2. "어째서?"라는 질문을 자주 하며 호기심이 많습니다.
            3. 철학적 통찰을 단순하게 표현합니다.
            4. "어른들은 참 이상해요"라는 표현을 씁니다.
            5. B-612 소행성에서 왔으며 장미와의 관계를 언급합니다.
            6. 여우의 "길들임"과 "책임"에 대한 교훈을 중요시합니다.
            7. "중요한 것은 눈에 보이지 않아"라는 문장을 사용합니다.
            8. 공손하고 친절한 말투를 사용합니다.
            9. 비유와 은유로 복잡한 개념을 설명합니다.
                
            항상 간결하게 답변하세요. 길어야 2-3문장으로 응답하고, 어린 왕자의 순수함과 지혜를 담아내세요.
            복잡한 주제도 본질적으로 단순화하여 설명하세요.""";
        SystemMessage systemMessage = SystemMessage.builder()
                .text(littlePricePersona)
                .build();

        // 사용자 메시지 생성
        UserMessage userMessage = UserMessage.builder()
                .text(question)
                .build();

        // 대화 옵션 설정
        ChatOptions chatOptions = ChatOptions.builder()
                .model("gpt-4o-mini")
                .temperature(0.3)
                .maxTokens(1000)
                .build();

        // 프롬프트 생성
        Prompt prompt = Prompt.builder()
                .messages(systemMessage, userMessage)
                .chatOptions(chatOptions)
                .build();

        // LLM에게 요청하고 응답받기
        Flux<ChatResponse> response = chatModel.stream(prompt);
        Flux<String> fluxString = response.map(res -> {
            AssistantMessage assistantMessage = res.getResult().getOutput();
            String chunk = Objects.requireNonNullElse(assistantMessage.getText(), "");
            return chunk;
        });

        return fluxString;
    }
}
