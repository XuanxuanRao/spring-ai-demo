package org.example.ai.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum BusinessType {
    CHAT("chat", "AI聊天");

    private final String code;
    private final String desc;

    public static BusinessType fromCode(String code) {
        return Arrays.stream(values())
                .filter(type -> type.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown business type code: " + code));
    }
}
