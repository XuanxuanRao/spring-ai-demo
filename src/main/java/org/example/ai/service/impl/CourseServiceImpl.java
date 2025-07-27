package org.example.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ai.domain.po.Course;
import org.example.ai.mapper.CourseMapper;
import org.example.ai.service.ICourseService;
import org.springframework.stereotype.Service;

/**
 * @author chenxuanrao06@gmail.com
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
}
