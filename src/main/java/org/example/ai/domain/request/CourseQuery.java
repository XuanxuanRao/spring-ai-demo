package org.example.ai.domain.request;

import lombok.Data;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.List;

/**
 * @author chenxuanrao06@gmail.com
 */
@Data
public class CourseQuery {
    @ToolParam(required = false, description = "课程类型：编程，设计，自媒体，其他")
    private String type;
    @ToolParam(required = false, description = "学历要求：0-无，1-初中，2-高中，3-大专，4-本科及以上")
    private Integer edu;
    @ToolParam(required = false, description = "排序方式")
    private List<Sort> sorts;

    @Data
    public static class Sort {
        @ToolParam(required = false, description = "排序字段：price-价格，duration-时长")
        private String field;
        @ToolParam(required = false, description = "排序方式：true-升序，false-降序")
        private Boolean asc;
    }
}
