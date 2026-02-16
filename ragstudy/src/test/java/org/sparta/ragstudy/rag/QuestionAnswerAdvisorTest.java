package org.sparta.ragstudy.rag;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class QuestionAnswerAdvisorTest {
    ChatClient chatClient;

    @Autowired
    ChatModel chatModel;

    @Autowired
    VectorStore vectorStore;

    @BeforeEach
    void init(@Autowired ChatClient.Builder builder) {
        chatClient = builder
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(Ordered.LOWEST_PRECEDENCE - 1)
                )
                .build();

    }

    @Test
    @DisplayName("PDF 파일 ETL 처리")
    void etlProcessing() {
        // E: 추출하기
        String dir = "/Users/yonggyo/Documents/lecture/document/";
        Resource resource = new FileSystemResource(dir + "대한민국헌법(19880225).pdf");
        DocumentReader reader = new PagePdfDocumentReader(resource);
        List<Document> documents = reader.read();

        // T: 메타데이터에 공통 정보 추가하기
        for (Document doc : documents) {
            Map<String, Object> metadata = doc.getMetadata();
            metadata.putAll(Map.of(
                    "title", "대한민국헌법",
                    "author", "법제처",
                    "source", "헌법"
            ));
        }

        // T: 작은 사이즈로 분할하기
        documents = transform(documents);

        // L: 적재하기
        vectorStore.add(documents);
    }


    // 작은 키워드로 분할하고 키워드 메타데이터를 추가하는 메서드
    private List<Document> transform(List<Document> documents) {
        List<Document> transformedDocuments = null;

        // 작게 분할하기
        DocumentTransformer splitter = new TokenTextSplitter(1000, 10, 10, 10000, false);
        transformedDocuments = splitter.apply(documents);

        // 메타데이터에 키워드 추가하기
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);
        transformedDocuments = keywordMetadataEnricher.apply(transformedDocuments);

        return transformedDocuments;
    }

    @Test
    @DisplayName("RAG 테스트")
    void ragTest() {
        String question = "대통령의 임기는?";

        // 벡터 저장소 검색 조건 생성
        SearchRequest searchRequest = SearchRequest.builder()
                .similarityThreshold(0.4)
                .topK(3)
                .filterExpression("source == '헌법'")
                .build();

        // QuestionAnswerAdvisor 생성
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(searchRequest)
                .build();

        // 프롬프트를 LLM으로 전송하고 응답 받기
        String answer = chatClient.prompt()
                .user(question)
                .advisors(questionAnswerAdvisor)
                .call()
                .content();

        log.info("답변: {}", answer);
    }
}
