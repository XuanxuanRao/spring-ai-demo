package org.example.ai.controller;

import lombok.RequiredArgsConstructor;
import org.example.ai.enums.BusinessType;
import org.example.ai.service.HistoryService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author chenxuanrao06@gmail.com
 */
@RestController
@RequestMapping("/ai")
@RequiredArgsConstructor
public class CustomerServiceController {
    private final ChatClient serviceClient;
    private final HistoryService historyService;

    @GetMapping(value = "/service", produces = "text/html;charset=utf-8")
    public Flux<String> service(@RequestParam String chatId, String prompt) {
        historyService.saveHistory(BusinessType.CUSTOMER_SERVICE, chatId);
        return serviceClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

}
