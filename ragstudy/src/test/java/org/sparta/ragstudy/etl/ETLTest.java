package org.sparta.ragstudy.etl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.tika.TikaDocumentReader;
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
public class ETLTest {
    @Autowired
    ChatModel chatModel;

    @Autowired
    VectorStore vectorStore;

    Resource textResource;
    Resource docResource;
    Resource pdfResource;

    String title = "대한민국헌법";
    String author = "법제처";

    @BeforeEach
    void init() {
        String dir = "/Users/yonggyo/Documents/lecture/document/";
        textResource = new FileSystemResource(dir + "대한민국헌법(19880225).txt");
        docResource = new FileSystemResource(dir + "대한민국헌법(19880225).docx");
        pdfResource = new FileSystemResource(dir + "대한민국헌법(19880225).pdf");
    }

    @Test
    @DisplayName("txt 파일")
    void ETL_text() {

        // E: 추출하기
        DocumentReader reader = new TextReader(textResource);
        List<Document> documents = reader.read();
        log.info("추출된 Document 수: {} 개", documents.size()); // 추출된 Document 수: 1 개

        // T: 메타데이터에 공통 정보 추가하기
        for (Document doc : documents) {
            Map<String, Object> metadata = doc.getMetadata();
            metadata.putAll(Map.of(
                    "title", title,
                    "author", author,
                    "source", "대한민국헌법(19880225).txt"
            ));
        }

        // T: 작은 사이즈로 분할하기
        documents = transform(documents);
        log.info("변환된 Document 수: {} 개", documents.size()); // 변환된 Document 수: 25 개

        // L: 적재하기
        vectorStore.add(documents);
    }


    @Test
    @DisplayName("docx 파일")
    void ETL_docx() {

        // E: 추출하기
        DocumentReader reader = new TikaDocumentReader(docResource);
        List<Document> documents = reader.read();
        log.info("추출된 Document 수: {} 개", documents.size()); // 추출된 Document 수: 1 개

        // T: 메타데이터에 공통 정보 추가하기
        for (Document doc : documents) {
            Map<String, Object> metadata = doc.getMetadata();
            metadata.putAll(Map.of(
                    "title", title,
                    "author", author,
                    "source", "대한민국헌법(19880225).docx"
            ));
        }

        // T: 작은 사이즈로 분할하기
        documents = transform(documents);
        log.info("변환된 Document 수: {} 개", documents.size()); // 변환된 Document 수: 25 개

        // L: 적재하기
        vectorStore.add(documents);
    }

    @Test
    @DisplayName("pdf 파일")
    void ETL_pdf() {

        // E: 추출하기
        DocumentReader reader = new PagePdfDocumentReader(pdfResource);
        List<Document> documents = reader.read();
        log.info("추출된 Document 수: {} 개", documents.size()); // 추출된 Document 수: 14 개

        // T: 메타데이터에 공통 정보 추가하기
        for (Document doc : documents) {
            Map<String, Object> metadata = doc.getMetadata();
            metadata.putAll(Map.of(
                    "title", title,
                    "author", author,
                    "source", "대한민국헌법(19880225).pdf"
            ));
        }

        // T: 작은 사이즈로 분할하기
        documents = transform(documents);
        log.info("변환된 Document 수: {} 개", documents.size()); // 변환된 Document 수: 40 개

        // L: 적재하기
        vectorStore.add(documents);
    }


    // 작은 키워드로 분할하고 키워드 메타데이터를 추가하는 메서드
    private List<Document> transform(List<Document> documents) {
        List<Document> transformedDocuments = null;

        // 작게 분할하기
        DocumentTransformer splitter = new TokenTextSplitter();
        transformedDocuments = splitter.apply(documents);

        // 메타데이터에 키워드 추가하기
        KeywordMetadataEnricher keywordMetadataEnricher = new KeywordMetadataEnricher(chatModel, 5);
        transformedDocuments = keywordMetadataEnricher.apply(transformedDocuments);

        return transformedDocuments;
    }
}
