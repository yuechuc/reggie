package com.yuechu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.common.CustomException;
import com.yuechu.entity.Category;
import com.yuechu.entity.Dish;
import com.yuechu.entity.Setmeal;
import com.yuechu.mapper.CategoryMapper;
import com.yuechu.service.CategoryService;
import com.yuechu.service.DishService;
import com.yuechu.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //查询当前分类是否关联了菜品，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Dish> dishServiceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishServiceLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int countDish = dishService.count(dishServiceLambdaQueryWrapper);
        if (countDish > 0){
            throw new CustomException ("当前分类下关联了菜品，不能删除！");
        }


        //查询当前分类是否关联了套餐，如果已经关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int countSetmeal = setmealService.count(setmealLambdaQueryWrapper);
        if (countDish > 0){
            throw new CustomException ("当前分类下关联了套餐，不能删除！");
        }


        //正常删除分类
        super.removeById(id);
    }
}
