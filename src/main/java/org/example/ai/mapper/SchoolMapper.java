package org.example.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.ai.domain.po.School;

@Mapper
public interface SchoolMapper extends BaseMapper<School> {
}
