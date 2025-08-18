package org.example.ai.controller;


import lombok.RequiredArgsConstructor;
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
public class GameController {

    private final ChatClient gameClient;

    @GetMapping(value = "game", produces = "text/html;charset=utf-8")
    public Flux<String> chat(@RequestParam String chatId, String prompt) {
        return gameClient.prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }

}
