package org.example.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ai.domain.po.CourseReservation;
import org.example.ai.mapper.CourseReservationMapper;
import org.example.ai.service.ICourseReservationService;
import org.springframework.stereotype.Service;

/**
 * @author chenxuanrao06@gmail.com
 */
@Service
public class CourseReservationServiceImpl extends ServiceImpl<CourseReservationMapper, CourseReservation> implements ICourseReservationService {
}
