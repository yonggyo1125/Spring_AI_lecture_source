package org.sparta.ragstudy.vectorestore;

import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DocumentSearchTest {
    @Autowired
    VectorStore vectorStore;

    @Test
    void searchTest1() {
        String question = "대통령은 얼마 동안 근무해?";
        List<Document> documents = vectorStore.similaritySearch(question);

        documents.forEach(System.out::println);
    }

    @Test
    void searchTest2() {
        String question = "대통령은 얼마 동안 근무해?";
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(question)
                        .topK(1) // 최상위 1개만 가져오기
                        .similarityThreshold(0.4) // 유사도 점수가 0.4 이상
                        .filterExpression("source == '헌법' && year >= 1987") // 메타데이터 검색 조건
                        .build()
        );

        documents.forEach(System.out::println);
    }
}
