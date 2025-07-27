package org.example.ai.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;

import java.io.IOException;
import java.util.*;

/**
 * @author chenxuanrao06@gmail.com
 */
@Slf4j
public class MessageDeserializer extends JsonDeserializer<Message> {

    public Message deserialize(JsonParser p, DeserializationContext ctxt) {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node;
        Message message = null;
        try {
            node = mapper.readTree(p);
            String messageType = node.get("messageType").asText();
            switch (messageType) {
                case "USER" -> {
                    Map<String, Object> metaData = mapper.convertValue(node.get("metadata"), new TypeReference<Map<String, Object>>() {
                    });
                    Collection<Media> media = mapper.convertValue(node.get("media"), new TypeReference<Collection<Media>>() {
                    });
                    return UserMessage.builder()
                            .text(node.get("text").asText())
                            .media((List<Media>) media)
                            .metadata(metaData)
                            .build();
                }
                case "ASSISTANT" -> message = new AssistantMessage(node.get("text").asText(),
                        mapper.convertValue(node.get("metadata"), new TypeReference<>() {
                        }), (List<AssistantMessage.ToolCall>) mapper.convertValue(node.get("toolCalls"),
                        new TypeReference<Collection<AssistantMessage.ToolCall>>() {
                        }),
                        (List<Media>) mapper.convertValue(node.get("media"), new TypeReference<Collection<Media>>() {
                        })
                );
                default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
            }
        }
        catch (IOException e) {
            log.error("Error deserializing message", e);
        }
        return message;
    }

}