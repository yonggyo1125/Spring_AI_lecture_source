package org.sparta.ragstudy.rag;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.util.StringUtils;

@Slf4j
@SpringBootTest
public class TranslationQueryTransformerTest {

    ChatClient chatClient;

    @Autowired
    ChatModel chatModel; // 새로운 ChatClientBuilder를 생성할 때 필요

    @Autowired
    VectorStore vectorStore; // VectorStoreDocumentRetriever를 생성할 때 필요

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();
    }

    @Test
    void chatWithTranslationTest() {
        double score = 0.3;
        String source = "헌법";
        String question = "Wie lange dauert die Amtszeit des Präsidenten?";

        // RetrievalAugmentationAdvisor 생성
        RetrievalAugmentationAdvisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(getTranslationQueryTransformer())
                .documentRetriever(getVectorStoreDocumentRetriever(score, source))
                .build();

        // 프롬프트를 LLM으로 전송하고 응답 받기
        String answer = chatClient.prompt()
                .user(question)
                .advisors(retrievalAugmentationAdvisor)
                .call()
                .content();

        log.info("답변: {}", answer);
    }

    // TranslationQueryTransformer를 생성하고 가져오는 메서드
    private TranslationQueryTransformer getTranslationQueryTransformer() {
            // 사용자의 질문을 번역하기 위해서는 원래 사용자 질문을 위한 ChatClient와는 별도로 새로운 ChatClient를 사용해야 함
            // 번역된 질문을 볼 수 있도록 SimpleLoggerAdvisor 추가
            ChatClient.Builder builder = ChatClient.builder(chatModel)
                    .defaultAdvisors(
                            new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                    );

            // 질문 번역기 생성
            return TranslationQueryTransformer.builder()
                    .chatClientBuilder(builder)
                    .targetLanguage("korean")
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
