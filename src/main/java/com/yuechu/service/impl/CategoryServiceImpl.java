package com.yuechu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.entity.Category;
import com.yuechu.entity.Employee;
import com.yuechu.mapper.CategoryMapper;
import com.yuechu.mapper.EmployeeMapper;
import com.yuechu.service.CategoryService;
import com.yuechu.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
