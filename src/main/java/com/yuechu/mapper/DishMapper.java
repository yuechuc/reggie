package com.yuechu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yuechu.entity.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Service;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
