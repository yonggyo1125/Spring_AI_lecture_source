package org.sparta.aistudy.prompt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
public class SelfConsistencyTest {
    ChatClient chatClient;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Test
    void test() {
        String answer = getConsistentSentiment("이 책은 초보자가 보기에 너무 어렵지만 열심히 노력한다면 어느정도 이해할수 있지 않을까 한다. 나한테는 어려웠음");
        log.info("답변: {}", answer);
    }

    public String getConsistentSentiment(String review) {
        int sampleCount = 5; // 5번의 샘플링

        // 1. 여러 번의 요청을 수행하여 응답 리스트 생성
        List<String> votes = IntStream.range(0, sampleCount)
                .mapToObj(i ->  fetchSentiment(review))
                .toList();

        // 2. 다수결 집계 로직
        return votes.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("판단 불가");
    }

    private String fetchSentiment(String review) {
        return chatClient.prompt()
                .system("""
                        당신은 리뷰 분석가입니다. 응답은 반드시 [긍정, 부정]중 하나로 분류하세요.
                        레이블만 반환하세요.
                        내용:
                        """)
                .user(review)
                .options(ChatOptions.builder()
                        .model("gpt-4o-mini")
                        .temperature(0.7) // 일관성 검증을 위해 변동성 부여
                        .build())
                .call()
                .content();
    }
}
