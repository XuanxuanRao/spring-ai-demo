package org.example.ai.config;

import jakarta.annotation.Resource;
import org.example.ai.constant.SystemConstant;
import org.example.ai.memory.RedisChatMemory;
import org.example.ai.tools.CourseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author chenxuanrao06@gmail.com
 */
@Configuration
public class AIConfig {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Bean
    public ChatClient chatClient(OpenAiChatModel model) {
        return ChatClient
                .builder(model)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory()).build()
                )
                .build();
    }

    @Bean
    public ChatClient gameClient(OpenAiChatModel model) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstant.GAME_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory()).build()
                )
                .build();
    }

    @Bean
    public ChatClient serviceClient(OpenAiChatModel model, CourseTool courseTool) {
        return ChatClient
                .builder(model)
                .defaultSystem(SystemConstant.SERVICE_SYSTEM_PROMPT)
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory()).build()
                )
                .defaultTools(courseTool)
                .build();
    }

    @Bean
    public ChatClient pdfClient(OpenAiChatModel model, ChatMemory chatMemory, VectorStore vectorStore) {
        return ChatClient
                .builder(model)
                .defaultSystem("请根据上下文回答问题，遇到上下文没有的问题，不要随意编造。")
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .similarityThreshold(0.5)
                                        .topK(2)
                                        .build())
                                .build()
                )
                .build();
    }

    @Bean
    public ChatMemory chatMemory() {
        return new RedisChatMemory(stringRedisTemplate);
    }

}
