package org.example.ai.tools;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import org.example.ai.domain.po.Course;
import org.example.ai.domain.po.CourseReservation;
import org.example.ai.domain.po.School;
import org.example.ai.domain.request.CourseQuery;
import org.example.ai.service.ICourseReservationService;
import org.example.ai.service.ICourseService;
import org.example.ai.service.ISchoolService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenxuanrao06@gmail.com
 */
@Component
@RequiredArgsConstructor
public class CourseTool {

    private final ICourseService courseService;
    private final ISchoolService schoolService;
    private final ICourseReservationService courseReservationService;

    @Tool(description = "根据条件查询课程")
    public List<Course> queryCourse(@ToolParam(description = "查询的条件") CourseQuery query) {
        LambdaQueryChainWrapper<Course> wrapper = courseService.lambdaQuery()
                .eq(StrUtil.isNotBlank(query.getType()), Course::getType, query.getType())
                .le(query.getEdu() != null && query.getEdu() > 0, Course::getEdu, query.getEdu());

        if (CollectionUtil.isNotEmpty(query.getSorts())) {
            query.getSorts().forEach(sort -> {
                if ("price".equals(sort.getField())) {
                    wrapper.orderBy(true, sort.getAsc() != null && sort.getAsc(), Course::getPrice);
                } else if ("duration".equals(sort.getField())) {
                    wrapper.orderBy(true, sort.getAsc() != null && sort.getAsc(), Course::getDuration);
                }
            });
        }

        return wrapper.list();
    }

    @Tool(description = "查询所有校区")
    public List<School> querySchool() {
        return schoolService.list();
    }


    @Tool(description = "生成预约单，返回预约单号")
    public Long createReservation(@ToolParam(description = "预约的课程") String course,
                                  @ToolParam(description = "预约的校区") String schoolName,
                                  @ToolParam(description = "预约人姓名") String studentName,
                                  @ToolParam(description = "预约人联系方式") String contactInfo,
                                  @ToolParam(required = false, description = "预约备注") String remark) {
        CourseReservation reservation = new CourseReservation();
        reservation.setCourse(course);
        reservation.setSchool(schoolName);
        reservation.setStudentName(studentName);
        reservation.setContactInfo(contactInfo);
        reservation.setRemark(remark);
        courseReservationService.save(reservation);
        return reservation.getId();
    }

}
