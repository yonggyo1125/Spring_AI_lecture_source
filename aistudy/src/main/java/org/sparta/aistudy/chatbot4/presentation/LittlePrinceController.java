package org.sparta.aistudy.chatbot4.presentation;

import jakarta.servlet.http.HttpSession;
import org.sparta.aistudy.chatbot4.application.LittlePrinceChatBotService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller("littlePrinceController4")
@RequestMapping("/littleprince4")
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
    public String message(@RequestBody String question, HttpSession session) {
        return service.generate(question, session.getId());
    }

    // 채팅 창 출력
    @GetMapping
    public String index() {
        return "chatbot4/index";
    }
}
