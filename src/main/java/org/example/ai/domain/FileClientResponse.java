package org.example.ai.domain;

import lombok.Data;

/**
 * @author chenxuanrao06@gmail.com
 */
@Data
public class FileClientResponse {
    /**
     * 响应码: SUCCESS(1) 代表成功; ERROR(0) 代表失败
     */
    private Integer code;
    /**
     * 响应信息，一个字符串用于描述响应结果
     */
    private String msg;
    /**
     * 返回的数据
     */
    private FileVO data;

    @Data
    public static class FileVO {
        private String name;
        private String url;
    }

    public static final Integer SUCCESS = 1;
    public static final Integer ERROR = 0;

}
