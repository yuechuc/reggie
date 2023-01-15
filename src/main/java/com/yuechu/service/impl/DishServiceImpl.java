package com.yuechu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.dto.DishDto;
import com.yuechu.entity.Dish;
import com.yuechu.entity.DishFlavor;
import com.yuechu.mapper.DishMapper;
import com.yuechu.service.DishFlavorService;
import com.yuechu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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


    //根据id查询菜品和口味
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //查询当前菜品对应的口味信息，从dish_flavor表查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);

        return dishDto;
    }

    //同时修改菜品和口味信息
    @Override
    public void updateWithFlavor(DishDto dishDto) {
//        1. 更新菜品信息 dish
        this.updateById(dishDto);

//        2. 清理当前口味信息 dish_flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

//        3. 添加提交的口味信息
        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors.forEach(f->f.setDishId(dishDto.getId()));
        dishFlavorService.saveBatch(flavors);
    }
}
