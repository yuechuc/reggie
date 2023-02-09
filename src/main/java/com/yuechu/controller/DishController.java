package com.yuechu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuechu.common.R;
import com.yuechu.dto.DishDto;
import com.yuechu.entity.Category;
import com.yuechu.entity.Dish;
import com.yuechu.entity.DishFlavor;
import com.yuechu.entity.Employee;
import com.yuechu.service.CategoryService;
import com.yuechu.service.DishFlavorService;
import com.yuechu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.awt.datatransfer.DataFlavor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    //新增菜品
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
//        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("success");
    }

    //菜品分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {


        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //添加过滤条件
        queryWrapper.like(name != null, Dish::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //获取分类id
            Long categoryId = item.getCategoryId();
            //获取分类名称
            Category category = categoryService.getById(categoryId);

            if (category != null) {
                String categoryName = category.getName();
                //保存分类名
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }


    //根据id查询菜品和口味
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);
    }


    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
//        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return R.success("success");
    }

    //菜品展示
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//
//        //查询菜品名
//        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
//        // status 为 起售  状态
//        queryWrapper.eq(Dish::getStatus, 1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//
//        return R.success(list);
//    }


    //菜品展示
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, dish.getStatus());
        List<Dish> dishList = dishService.list(queryWrapper);

        List<DishDto> dishDtoList= dishList.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> dishFlavors = dishFlavorService.list(wrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());

        return R.success(dishDtoList);
    }

    //菜品停售、起售
    @PostMapping("/status/{status}")
    public R<String> updateDishStatus(HttpServletRequest request,@PathVariable int status,String[]  ids) {
        // employee.setUpdateTime(LocalDateTime.now());
        for(String id: ids){
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dish.setUpdateTime(LocalDateTime.now());
            dish.setUpdateUser((Long) request.getSession().getAttribute("employee"));
            dishService.updateById(dish);
        }

        return R.success("菜品信息修改成功！");
    }

    //删除菜品
    @DeleteMapping
    public R<String> delete(String[]  ids) {
        // employee.setUpdateTime(LocalDateTime.now());
        int index=0;
        for(String id:ids) {
            Dish dish = dishService.getById(id);
            if(dish.getStatus()!=1){
                dishService.removeById(id);
            }else {
                index++;
            }
        }
        if (index>0&&index==ids.length){
            return R.error("选中的菜品均为启售状态，不能删除");
        }else {
            return R.success("删除成功");
        }
    }



}
