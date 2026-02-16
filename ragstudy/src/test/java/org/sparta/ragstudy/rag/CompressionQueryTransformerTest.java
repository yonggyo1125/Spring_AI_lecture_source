package org.sparta.ragstudy.rag;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.CompressionQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
public class CompressionQueryTransformerTest {
    ChatClient chatClient;

    @Autowired
    ChatModel chatModel; // 새로운 ChatClientBuilder를 생성할 때 필요

    @Autowired
    VectorStore vectorStore; // VectorStoreDocumentRetriever를 생성할 때 필요

    @Autowired
    ChatMemory chatMemory; // MessageChatMemoryAdvisor에서 대화 기억을 프롬프트에 추가할 때 필요

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
    }

    @Test
    void chatWithCompressionTest() {
        String question1 = "대통령의 임기는 어떻게 됩니까?";
        String question2 = "국회 의원은?";
        double score = 0.3;
        String conversationId = UUID.randomUUID().toString();

        // RetrievalAugmentationAdvisor 생성
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(getCompressionQueryTransformer()) // CompressionQueryTransformer 추가
                .documentRetriever(getVectorStoreDocumentRetriever(score, "헌법")) // VectorStoreDocumentRetriever 추가
                .build();

        // 프롬프트를 LLM으로 전송하고 응답 받기
        List.of(question1, question2).forEach(question -> {
            String answer = chatClient.prompt()
                    .user(question)
                    .advisors(
                            MessageChatMemoryAdvisor.builder(chatMemory).build(), // 대화 기록을 프롬프트에 추가
                            retrievalAugmentationAdvisor
                    )
                    .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)) // MessageChatMemoryAdvisor에서 사용할 대화 ID를 Advisor 공유 데이터에 ChatMemory.CONVERSATION_ID를 키로 하여 conversationId 값으로 저장
                    .call()
                    .content();

            log.info("답변: {}", answer);
        });
    }

    // CompressionQueryTransformer를 생성하고 가져오는 메서드
    private CompressionQueryTransformer getCompressionQueryTransformer() {
        // 사용자의 질문을 완전한 질문으로 만들기 위해서는 원래 사용자 질문을 위한 ChatClient와는 별도로 새로운 ChatClient를 사용해야 합니다. 즉, 새로운 ChatClient.Builder가 필요
        // 변환된 완전한 질문을 확인하기 위해 SimpleLoggerAdvisor 추가
        ChatClient.Builder builder = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                );

        // 압축 쿼리 변환기 생성
        return CompressionQueryTransformer.builder()
                .chatClientBuilder(builder)
                .build();
    }

    //  VectorStoreDocumentRetriever 생성하고 가져오는 메서드
    private VectorStoreDocumentRetriever getVectorStoreDocumentRetriever(double score, String source) {
        return VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)  // 유사도 검색을 수행하기 위한 벡터 저장소
                .similarityThreshold(score) // 유사도 임계점수
                .topK(3) // 상위 3개
                .filterExpression(() -> { // 메타데이터 필터링
                    // 출처(source)를 보내면, 동일한 출처인 Document만 유사도 검색을 수행합니다.
                    // 출처가 없으면 전체 Document를 대상으로 유사도 검색을 수행합니다.
                    FilterExpressionBuilder builder = new FilterExpressionBuilder();
                    return StringUtils.hasText(source) ? builder.eq("source", source).build() : null;
                })
                .build();
    }
}
