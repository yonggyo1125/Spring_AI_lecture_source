package org.sparta.aistudy.chatbot2.presentation;

import org.sparta.aistudy.chatbot2.application.LittlePrinceChatBotService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Controller("littlePrinceController2")
@RequestMapping("/littleprince2")
public class LittlePrinceController {

    private final LittlePrinceChatBotService service;

    public LittlePrinceController(LittlePrinceChatBotService service) {
        this.service = service;
    }

    // 채팅 메세지 수신 및 응답 생성
    @ResponseBody
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public String message(@RequestBody String question) {
        return service.generate(question);
    }

    // 채팅 창 출력
    @GetMapping
    public String index() {
        return "chatbot1/index";
    }


    // 채팅 메시지 수신 및 스트리밍 응답 생성
    @ResponseBody
    @PostMapping(
            path = "/stream",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_NDJSON_VALUE
    )
    public Flux<String> streamMessage(@RequestBody String question) {
        return service.generateStream(question);
    }

    @GetMapping("/stream")
    public String streamIndex() {
        return "chatbot1/stream_index.html";
    }
}
