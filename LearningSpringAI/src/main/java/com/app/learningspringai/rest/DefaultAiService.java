package com.app.learningspringai.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.retry.NonTransientAiException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Service
public class DefaultAiService implements AiService {

    private static final Logger log = LoggerFactory.getLogger(DefaultAiService.class);

    private final ChatClient chatClient;

    public DefaultAiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Ask the underlying AI model. If a model name is provided (non-blank) it overrides the
     * default model configured via application.properties. Otherwise the globally configured
     * default model is used. Blank prompts trigger a validation exception handled by the
     * GlobalExceptionHandler.
     *
     * @param prompt user input (required, non blank)
     * @param model optional model name; blank -> use configured default
     * @return textual answer from the AI provider (never null, may be empty if provider returns no content)
     */
    @Override
    public String ask(String prompt, String model) {
        if (!StringUtils.hasText(prompt)) {
            throw new IllegalArgumentException("Prompt must not be blank");
        }

        Prompt effectivePrompt;
        if (StringUtils.hasText(model)) {
            OpenAiChatOptions options = OpenAiChatOptions.builder().model(model.trim()).build();
            effectivePrompt = new Prompt(prompt, options);
        } else {
            // rely on default model & options from configuration
            effectivePrompt = new Prompt(prompt);
        }

        try {
            String resultText = Objects.requireNonNull(chatClient.prompt(effectivePrompt)
                    .user(prompt)
                            .options(OpenAiChatOptions.builder().temperature(1.0).build())
                    .call().chatResponse()).getResult().getOutput().getText();


            if (resultText == null) {
                log.warn("AI response text was null for prompt length={}", prompt.length());
                return ""; // normalize null to empty string
            }
            return resultText;
        } catch (NonTransientAiException ex) {
            // Non-retryable provider-side error (e.g., unsupported parameter). Surface a concise message.
            log.error("NonTransient AI error for prompt prefix='{}': {}", safePrefix(prompt), ex.getMessage());
            throw new IllegalStateException("AI provider error: " + ex.getMessage(), ex);
        } catch (RuntimeException ex) {
            // Unexpected errors (serialization, network, etc.)
            log.error("Unexpected error calling AI for prompt prefix='{}'", safePrefix(prompt), ex);
            throw ex;
        }
    }

    private String safePrefix(String prompt) {
        int max = Math.min(prompt.length(), 30);
        return prompt.substring(0, max).replaceAll("\n", " ") + (prompt.length() > max ? "…" : "");
    }
}
