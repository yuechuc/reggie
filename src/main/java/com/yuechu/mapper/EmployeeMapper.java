package com.yuechu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuechu.entity.Employee;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
