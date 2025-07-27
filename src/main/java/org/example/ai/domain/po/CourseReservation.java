package org.example.ai.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author chenxuanrao06@gmail.com
 */
@TableName("course_reservation")
@Data
public class CourseReservation implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String course;
    private String studentName;
    private String contactInfo;
    private String school;
    private String remark;
}
