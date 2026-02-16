package org.sparta.ragstudy.etl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.reader.jsoup.JsoupDocumentReader;
import org.springframework.ai.reader.jsoup.config.JsoupDocumentReaderConfig;
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
public class ETLHTMLTest {

    @Autowired
    VectorStore vectorStore;

    @Test
    void etlTest() {
        // E: 추출하기
        String path = "/Users/yonggyo/Documents/lecture/document/대한민국헌법(19880225).html";

        String title = "대한민국헌법";
        String author = "법제처";

        Resource resource = new FileSystemResource(path);

        JsoupDocumentReader reader = new JsoupDocumentReader(
                resource,
                JsoupDocumentReaderConfig.builder()
                        .charset("UTF-8")
                        .selector("#content")
                        .additionalMetadata(
                                Map.of(
                                    "title", title,
                                    "author", author,
                                    "source", "대한민국헌법(19880225).html"
                                )
                        )
                        .build());

        List<Document> documents = reader.read();
        log.info("추출된 Document 수: {} 개", documents.size()); // 추출된 Document 수: 1 개

        // T: 변환하기
        DocumentTransformer splitter = new TokenTextSplitter();
        List<Document> transformedDocuments = splitter.apply(documents);
        log.info("변환된 Document 수: {}", transformedDocuments.size()); // 변환된 Document 수: 26

        // L: 적재하기
        vectorStore.add(transformedDocuments);

    }

}
