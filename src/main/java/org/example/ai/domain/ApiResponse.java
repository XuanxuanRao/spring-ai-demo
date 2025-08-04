package org.example.ai.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 对请求的响应结果进行封装，在项目中，所有的请求都以 ApiResponse 的形式返回给前端
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    /**
     * 响应码: SUCCESS(1) 代表成功; ERROR(0) 代表失败
     */
    private Integer ok;
    /**
     * 响应信息，一个字符串用于描述响应结果
     */
    private String msg;
    /**
     * 返回的数据
     */
    private T data;

    public static final Integer SUCCESS = 1;
    public static final Integer ERROR = 0;

    /**
     * 增删改类请求成功响应
     * @return Result
     */
    public static <T> ApiResponse<T> success(){
        return new ApiResponse<>(SUCCESS, "success", null);
    }

    /**
     * 查询类请求成功响应
     * @param data 要返回的数据（查询的结果）
     * @return Result
     */
    public static <T> ApiResponse<T> success(T data){
        return new ApiResponse<>(SUCCESS, "success", data);
    }

    /**
     * 响应失败，返回错误信息
     * @param msg 错误信息
     * @return Result
     */
    public static <T> ApiResponse<T> error(String msg){
        return new ApiResponse<>(ERROR, msg, null);
    }

    public static <T> ApiResponse<T> error(String msg, T data){
        return new ApiResponse<>(ERROR, msg, data);
    }
}
