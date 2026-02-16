package org.sparta.tools.datetime;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class DateTimeServiceTest {

    @Autowired
    DateTimeService service;

    @Test
    void test1() {
        String answer = service.chat("내일 날짜는 어떻게 돼?");

        log.info("답변: {}", answer);
    }

    @Test
    void test2() {
        String answer = service.chat("지금부터 1시간 뒤에 알림이 울리도록 설정해줘");

        log.info("답변: {}", answer);
    }
}
