package org.sparta.ragstudy.embedding;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.embedding.EmbeddingResponseMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class TextEmbeddingTest {

    @Autowired
    EmbeddingModel embeddingModel;

    @Test
    void textEmbeddingTest() {

        // 임베딩하기
        EmbeddingResponse response = embeddingModel.embedForResponse(List.of("고양이", "강아지"));

        // 임베딩 모델 정보 얻기
        EmbeddingResponseMetadata metadata = response.getMetadata();
        log.info("모델 이름: {}", metadata.getModel());
        log.info("모델의 임베딩 차원: {}", embeddingModel.dimensions());

        // 임베딩 결과 얻기
        Embedding embedding = response.getResults().get(0);
        log.info("벡터 차원: {}", embedding.getOutput().length);
        log.info("벡터: {}", embedding.getOutput());

    }

    @Test
    void textEmbeddingTest2() {
        // 임베딩하기
        float[] vector = embeddingModel.embed("고양이");
        log.info("벡터 차원: {}", vector.length);
        log.info("벡터: {}", vector);
    }
}
