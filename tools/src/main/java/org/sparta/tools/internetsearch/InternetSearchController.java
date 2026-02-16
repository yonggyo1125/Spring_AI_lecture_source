package org.sparta.tools.internetsearch;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tools/search")
public class InternetSearchController {

    private final InternetSearchService internetSearchService;

    public InternetSearchController(InternetSearchService internetSearchService) {
        this.internetSearchService = internetSearchService;
    }

    @GetMapping
    public String index() {
        return "search/index";
    }

    @PostMapping
    @ResponseBody
    public String search(@RequestBody String question) {
        return internetSearchService.chat(question);
    }
}
