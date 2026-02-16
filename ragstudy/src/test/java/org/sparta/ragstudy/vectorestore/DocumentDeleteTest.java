package org.sparta.ragstudy.vectorestore;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DocumentDeleteTest {

    @Autowired
    VectorStore vectorStore;

    @Test
    void deleteTest() {
        vectorStore.delete("source == '헌법' && year < 1987");
    }
}
