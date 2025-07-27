package org.example.ai.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author chenxuanrao06@gmail.com
 */
@TableName("course")
@Data
public class Course implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer edu;
    private String type;
    private BigInteger price;
    private Integer duration;
}
