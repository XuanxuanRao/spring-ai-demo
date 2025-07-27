package org.example.ai.domain;

import lombok.Data;
import org.example.ai.enums.BusinessType;

import java.time.LocalDateTime;

/**
 * @author chenxuanrao06@gmail.com
 */
@Data
public class History {
    private String id;
    private BusinessType type;
    private LocalDateTime lastAccessTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
