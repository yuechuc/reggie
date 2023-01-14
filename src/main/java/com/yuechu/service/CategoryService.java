package com.yuechu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuechu.entity.Category;
import com.yuechu.entity.Employee;

public interface CategoryService extends IService<Category> {

    void remove(Long id);
}