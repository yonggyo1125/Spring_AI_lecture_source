package org.sparta.tools.heatingsystem;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class HeatingSystemServiceTest {

    @Autowired
    HeatingSystemService service;

    @Test
    void test() {
        String answer = service.chat("현재 온도를 23도 유지해줘");
        log.info("답변: {}", answer);
    }
}
