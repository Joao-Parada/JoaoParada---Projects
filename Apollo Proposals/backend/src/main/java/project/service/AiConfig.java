package project.service;

import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.api.OpenAiApi;

@Configuration
public class AiConfig {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${ai.chat.model:gpt-4o}")
    private String model;

    @Value("${ai.temperature:0.4}")
    private float temperature;

    @Bean
    public ChatClient chatClient() {
        OpenAiApi openAiApi = new OpenAiApi(apiKey);
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .withModel(model)
                .withTemperature(temperature)
                .build();
        return new OpenAiChatClient(openAiApi, options);
    }
}