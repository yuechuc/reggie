package com.yuechu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.dto.DishDto;
import com.yuechu.entity.Dish;
import com.yuechu.entity.DishFlavor;
import com.yuechu.mapper.DishMapper;
import com.yuechu.service.DishFlavorService;
import com.yuechu.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional //控制两张表 需要打开事务控制
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    //新增菜品，同事保存口味数据
    @Override
    public void saveWithFlavor(DishDto dishDto) {
//        保存菜品信息到dish
        this.save(dishDto);

        //获取菜品id
        Long dishId = dishDto.getId();
//生成口味列表
        List<DishFlavor> flavors = dishDto.getFlavors();

//设置口味对应的菜品id  方法1：
        flavors.forEach(f->f.setDishId(dishId));
//设置口味对应的菜品id  方法2：
//        flavors = flavors.stream().map((item) -> {
//           item.setDishId(dishId);
//           return item;
//       }).collect(Collectors.toList());

//        保存口味数据到dish_flavor
         dishFlavorService.saveBatch(flavors);

    }
}
