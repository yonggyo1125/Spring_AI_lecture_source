package org.sparta.ragstudy.etl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.JsonMetadataGenerator;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;

@Slf4j
@SpringBootTest
public class ETLJSONTest {

    @Autowired
    private VectorStore vectorStore;

    @Test
    void etlTest() {
        // E: 추출하기
        String path = "/Users/yonggyo/Documents/lecture/document/대한민국헌법(19880225).json";

        String title = "대한민국헌법";
        String author = "법제처";

        Resource resource = new FileSystemResource(path);

        JsonReader reader = new JsonReader(
                resource,
                new JsonMetadataGenerator() {
                    @Override
                    public Map<String, Object> generate(Map<String, Object> jsonMap) {
                        return Map.of(
                                "title", jsonMap.get("title"),
                                "author", jsonMap.get("author"),
                                "source", "대한민국헌법(19880225).json");
                    }
                },
                "date", "content");
        List<Document> documents = reader.read();
        log.info("추출된 Document 수: {} 개", documents.size()); // 추출된 Document 수: 12 개

        // T: 변환하기
        DocumentTransformer splitter = new TokenTextSplitter();
        List<Document> transformedDocuments = splitter.apply(documents);
        log.info("변환된 Document 수: {} 개", transformedDocuments.size()); // 변환된 Document 수: 31 개

        // L: 적재하기
        vectorStore.add(transformedDocuments);
    }
}
