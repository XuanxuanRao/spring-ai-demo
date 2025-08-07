package org.example.ai.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.ai.chat.messages.Message;

/**
 * @author chenxuanrao06@gmail.com
 */
@Data
@NoArgsConstructor
@Schema(name = "聊天中发送的消息")
public class MessageVO {
    @Schema(description = "消息发送者角色, user or assistant", example = "user")
    private String role;
    private String content;

    public MessageVO(Message message) {
        switch (message.getMessageType()) {
            case USER -> role = "user";
            case ASSISTANT -> role = "assistant";
            default -> role = "";
        }
        content = message.getText();
    }

}
