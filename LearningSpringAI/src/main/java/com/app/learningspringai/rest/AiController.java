package com.app.learningspringai.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*") // allow calling from a different frontend origin during development
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }


    @GetMapping("/chat")
    public ResponseEntity<ChatResponse> chat(@RequestParam String prompt, @RequestParam(required = false) String model) {
        if (prompt == null || prompt.isBlank()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Prompt must not be blank"));
        }
        String answer = aiService.ask(prompt, model);
        return ResponseEntity.ok(new ChatResponse(answer));
    }
}
