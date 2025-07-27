package org.example.ai.memory;

import cn.hutool.core.collection.CollectionUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.constant.RedisConstant;
import org.example.ai.serializer.MessageDeserializer;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenxuanrao06@gmail.com
 */
@RequiredArgsConstructor
@Slf4j
public class RedisChatMemory implements ChatMemory {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final long MAX_MESSAGES_PER_CONVERSATION = 100;

    public RedisChatMemory(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Message.class, new MessageDeserializer());
        this.objectMapper.registerModule(module);
    }


    @Override
    public void add(String conversationId, List<Message> messages) {
        if (CollectionUtil.isEmpty(messages)) {
            log.debug("对话ID: {} 没有要添加的消息。", conversationId);
            return;
        }

        String key = String.format(RedisConstant.CHAT_MEMORY_KEY, conversationId);
        log.info("正在为对话ID: {} 添加 {} 条消息到 Redis 聊天内存。",
                conversationId, messages.size());

        try {
            for (Message message : messages) {
                // 使用 Jackson 序列化 Message 对象
                String messageJson = objectMapper.writeValueAsString(message);
                stringRedisTemplate.opsForList().rightPush(key, messageJson);
            }

            if (MAX_MESSAGES_PER_CONVERSATION > 0) {
                stringRedisTemplate.opsForList().trim(key, -MAX_MESSAGES_PER_CONVERSATION, -1);
                log.debug("对话ID: {} 的聊天内存已修剪至最新 {} 条消息。",
                        conversationId, MAX_MESSAGES_PER_CONVERSATION);
            }
        } catch (Exception e) {
            log.error("添加消息到 Redis 失败，对话ID: {}: {}", conversationId, e.getMessage(), e);
        }
    }

    @Override
    public List<Message> get(String conversationId) {
        String key = String.format(RedisConstant.CHAT_MEMORY_KEY, conversationId);
        log.info("正在从 Redis 聊天内存中检索对话ID: {} 的消息。", conversationId);

        List<String> messageJsons = stringRedisTemplate.opsForList().range(key, 0, -1);

        if (CollectionUtil.isEmpty(messageJsons)) {
            log.debug("对话ID: {} 没有找到消息。", conversationId);
            return List.of();
        }

        log.debug("从 Redis 为对话ID: {} 检索到 {} 条消息 JSON。", messageJsons.size(), conversationId);

        try {
            return messageJsons.stream()
                    .map(json -> {
                        try {
                            // 使用 Jackson 反序列化为 Message 对象
                            return objectMapper.readValue(json, Message.class);
                        } catch (Exception e) {
                            log.error("单条消息反序列化失败: {}", json, e);
                            return null; // 或者跳过这条消息
                        }
                    })
                    .filter(java.util.Objects::nonNull) // 过滤掉反序列化失败的消息
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("对话ID: {} 的消息列表反序列化失败: {}", conversationId, e.getMessage(), e);
            return List.of();
        }
    }

    @Override
    public void clear(String conversationId) {
        String key = String.format(RedisConstant.CHAT_MEMORY_KEY, conversationId);
        log.info("正在清除对话ID: {} 的聊天内存。", conversationId);
        Boolean deleted = stringRedisTemplate.delete(key);
        if (Boolean.TRUE.equals(deleted)) {
            log.info("对话ID: {} 的聊天内存已成功清除。", conversationId);
        } else {
            log.warn("未能清除对话ID: {} 的聊天内存，或该键不存在。", conversationId);
        }
    }
}