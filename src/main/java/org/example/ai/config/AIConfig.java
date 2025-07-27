package org.example.ai.config;

import jakarta.annotation.Resource;
import org.example.ai.constant.SystemConstant;
import org.example.ai.memory.RedisChatMemory;
import org.example.ai.tools.CourseTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
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
    public ChatMemory chatMemory() {
        return new RedisChatMemory(stringRedisTemplate);
    }

}
