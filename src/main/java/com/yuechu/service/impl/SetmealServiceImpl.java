package com.yuechu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.dto.SetmealDto;
import com.yuechu.entity.Category;
import com.yuechu.entity.Setmeal;
import com.yuechu.entity.SetmealDish;
import com.yuechu.mapper.SetmealMapper;
import com.yuechu.service.SetmealDishService;
import com.yuechu.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

//    @Autowired
//    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    //新增套餐，同时需要保存套餐和菜品的关联关系
    @Override
    public void saveWithDish(SetmealDto setmealDto) {

//        1.保存套餐基本信息 表setmeal
        this.save(setmealDto);
//        setmealService.save(setmealDto);

//        2.把菜品和套餐关联信息写入setmeal_dish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        //插入关联的套餐id
       setmealDishes.stream().map((item)->{
           item.setSetmealId(setmealDto.getId());
           return item;
       }).collect(Collectors.toList());

//       插入setmeal_dish表
        setmealDishService.saveBatch(setmealDishes);
    }
}
