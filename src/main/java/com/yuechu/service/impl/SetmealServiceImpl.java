package com.yuechu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuechu.common.CustomException;
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

    @Autowired
    private SetmealService setmealService;

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

//    删除套餐
    @Override
    public void removeWithDish(List<Long> ids) {
//        查询套餐状态，1启用，不可删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);

        if (count > 0 ){
            //        如果不能删除，抛出异常
            throw new CustomException("售卖中，不能删除!");
        }

//        可以删除，先删除套餐表中的数据 setmeal
        this.removeByIds(ids);
//        再删除setmealDish表中的鲨鱼夹 setmealDish
        LambdaQueryWrapper<SetmealDish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SetmealDish::getSetmealId,ids);
        setmealDishService.removeByIds(ids);
    }
}
