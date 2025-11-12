package com.app.learningspringai.rest;

import com.app.learningspringai.model.ChatRequest;
import com.app.learningspringai.model.ChatResponse;
import com.app.learningspringai.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // allow calling from a different frontend origin during development
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }


    @PostMapping("/ai/chat")
    public ResponseEntity<ChatResponse> chat(@RequestBody ChatRequest request) {
        if (request.prompt() == null || request.prompt().isBlank()) {
            return ResponseEntity.badRequest().body(new ChatResponse("Prompt must not be blank"));
        }
        String answer = aiService.ask(request.prompt(), request.model());
        return ResponseEntity.ok(new ChatResponse(answer));
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AI Service is up and running!");
    }
}
