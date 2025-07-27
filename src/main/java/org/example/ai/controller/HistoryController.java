package org.example.ai.controller;

import cn.hutool.core.collection.CollectionUtil;
import lombok.RequiredArgsConstructor;
import org.example.ai.domain.History;
import org.example.ai.domain.MessageVO;
import org.example.ai.enums.BusinessType;
import org.example.ai.service.HistoryService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chenxuanrao06@gmail.com
 */
@RestController
@RequestMapping("/ai/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    private final ChatMemory chatMemory;

    @GetMapping("/{type}")
    public List<String> getHistories(@PathVariable String type) {
        BusinessType businessType = BusinessType.fromCode(type);
        return historyService.getHistory(businessType)
                .stream()
                .map(History::getId)
                .toList();
    }

    @GetMapping("/{type}/{id}")
    public List<MessageVO> getHistory(@PathVariable String type, @PathVariable String id) {
        BusinessType businessType = BusinessType.fromCode(type);
        History history = historyService.getHistoryById(businessType, id);
        List<Message> messages = chatMemory.get(history.getId());
        if (CollectionUtil.isEmpty(messages)) {
            return List.of();
        }
        return messages.stream().map(MessageVO::new).toList();
    }

}
