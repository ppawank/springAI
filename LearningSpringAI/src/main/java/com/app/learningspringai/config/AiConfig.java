package com.app.learningspringai.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;

@Configuration
public class AiConfig {

    @Bean
    @ConditionalOnMissingBean
    public ChatClient chatClient(ChatModel chatModel) {
        // Build a ChatClient using the autoconfigured ChatModel (OpenAiChatModel)
        return ChatClient.builder(chatModel).build();
    }
}

