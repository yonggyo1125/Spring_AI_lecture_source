package org.sparta.tools.recommendmovie;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class RecommendMovieServiceTest {
    @Autowired
    RecommendMovieService service;

    @Test
    void test() {
        String answer = service.chat("bluesky가 좋아할만한 영화를 추천해줘");
        log.info("답변: {}", answer);
    }
}
