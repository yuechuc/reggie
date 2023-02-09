package com.yuechu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuechu.common.R;
import com.yuechu.dto.DishDto;
import com.yuechu.dto.SetmealDto;
import com.yuechu.entity.*;
import com.yuechu.service.CategoryService;
import com.yuechu.service.SetmealDishService;
import com.yuechu.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    //新增套餐功能
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveWithDish(setmealDto);
        return R.success("success");
    }


    //套餐信息分页查询功能
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){

        Page<Setmeal> setmealPage = new Page<>(page,pageSize);

        Page<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        //?????
        setmealService.page(setmealPage,queryWrapper);

        List<Setmeal> records = setmealPage.getRecords();
        log.info(records.toString());

        List<SetmealDto> list = records.stream().map((item)->{
            //对象拷贝
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if (category!=null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }


    // 删除套餐功能
    @DeleteMapping
    public R<String> deleteById(@RequestParam List<Long> ids){
        setmealService.removeWithDish(ids);
        return R.success("success");
    }


    //套餐展示
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> setmealList = setmealService.list(queryWrapper);

        return R.success(setmealList);
    }

    //套餐停售、起售
    @PostMapping("/status/{status}")
    public R<String> updateSetmealStatus(HttpServletRequest request, @PathVariable int status, String[]  ids) {
        // employee.setUpdateTime(LocalDateTime.now());
        for(String id: ids){
            Setmeal Setmeal = setmealService.getById(id);
            Setmeal.setStatus(status);
            Setmeal.setUpdateTime(LocalDateTime.now());
            Setmeal.setUpdateUser((Long) request.getSession().getAttribute("employee"));
            setmealService.updateById(Setmeal);
        }

        return R.success("套菜信息修改成功！");
    }

    // //删除套餐
    // @DeleteMapping
    // public R<String> delete(String[]  ids) {
    //     // employee.setUpdateTime(LocalDateTime.now());
    //     int index=0;
    //     for(String id:ids) {
    //         Setmeal setmeal = setmealService.getById(id);
    //         if(setmeal.getStatus()!=1){
    //             setmealService.removeById(id);
    //         }else {
    //             index++;
    //         }
    //     }
    //     if (index>0&&index==ids.length){
    //         return R.error("选中的套餐均为启售状态，不能删除");
    //     }else {
    //         return R.success("删除成功");
    //     }
    // }

}
