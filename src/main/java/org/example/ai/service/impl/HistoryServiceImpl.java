package org.example.ai.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.ai.constant.RedisConstant;
import org.example.ai.domain.History;
import org.example.ai.enums.BusinessType;
import org.example.ai.service.HistoryService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * @author chenxuanrao06@gmail.com
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryServiceImpl implements HistoryService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public List<History> getHistory(BusinessType type) {
        String key = String.format(RedisConstant.ALL_HISTORIES_KEY, type.getCode());
        Set<String> range = stringRedisTemplate.opsForZSet().reverseRange(key, 0, -1);
        if (CollectionUtil.isEmpty(range)) {
            return List.of();
        }
        return range.stream()
                .map(this::findById)
                .filter(history -> history != null && history.getType() == type)
                .toList();
    }

    @Override
    public History getHistoryById(BusinessType type, String id) {
        return findById(id);
    }

    @Override
    public void saveHistory(BusinessType type, String id) {
        History history = new History();
        history.setId(id);
        history.setType(type);
        history.setCreateTime(LocalDateTime.now());
        history.setUpdateTime(LocalDateTime.now());
        history.setLastAccessTime(LocalDateTime.now());
        String historyJson;
        try {
            historyJson = objectMapper.writeValueAsString(history);
        } catch (JsonProcessingException e) {
            log.error("Error serializing history with id: {}", id, e);
            return;
        }
        stringRedisTemplate.opsForValue().set(String.format(RedisConstant.HISTORY_KEY, id), historyJson);
        stringRedisTemplate.opsForZSet().add(String.format(RedisConstant.ALL_HISTORIES_KEY, type.getCode()), id, System.currentTimeMillis());
    }

    private History findById(String id) {
        String historyJson = stringRedisTemplate.opsForValue().get(String.format(RedisConstant.HISTORY_KEY, id));
        if (StrUtil.isBlank(historyJson)) {
            return null;
        }
        try {
            return objectMapper.readValue(historyJson, History.class);
        } catch (Exception e) {
            log.error("Error deserializing history with id: {}, content: {}", id, historyJson, e);
            return null;
        }
    }
}
