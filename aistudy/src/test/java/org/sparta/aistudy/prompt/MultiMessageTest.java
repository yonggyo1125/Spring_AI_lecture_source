package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class MultiMessageTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
// 1. 메시지 리스트 준비 (복수 메시지 구성을 위해 List 사용)
        List<Message> messageList = new ArrayList<>();

        // 2. 시스템 페르소나 설정
        messageList.add(new SystemMessage("당신은 친절한 요리사입니다. 사용자의 질문에 요리 비유를 들어 답변하세요."));

        // 3. 이전 대화 맥락 추가 (Assistant의 답변 이력 포함)
        messageList.add(new UserMessage("안녕? 첫 번째 질문이야. 오늘 날씨 어때?"));
        messageList.add(new AssistantMessage("안녕하세요! 오늘은 신선한 샐러드처럼 상큼한 날씨네요."));

        // 4. 현재 사용자의 새로운 질문 추가
        messageList.add(new UserMessage("그럼 이런 날씨엔 어떤 공부를 하면 좋을까?"));

        // 5. ChatClient를 통해 전체 메시지 리스트 전달
        String answer =  chatClient.prompt()
                .messages(messageList) // List<Message>를 통째로 전달
                .call()
                .content();

        log.info("답변: {}", answer);
    }
}
