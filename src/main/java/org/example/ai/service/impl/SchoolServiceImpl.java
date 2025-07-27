package org.example.ai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.ai.domain.po.School;
import org.example.ai.mapper.SchoolMapper;
import org.example.ai.service.ISchoolService;
import org.springframework.stereotype.Service;

/**
 * @author chenxuanrao06@gmail.com
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {
}
