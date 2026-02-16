package org.sparta.mcp.chatbot.presentation;

import org.sparta.mcp.chatbot.application.ChatbotService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@Controller
@RequestMapping("/chat")
public class ChatbotController {
    private final ChatbotService service;

    public ChatbotController(ChatbotService service) {
        this.service = service;
    }

    @GetMapping
    public String index() {
        return "chatbot/index";
    }

    @ResponseBody
    @PostMapping(
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public Flux<String> chatProcess(@RequestBody String question) {
        return service.chat(question);
    }
}
