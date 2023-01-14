package com.yuechu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuechu.common.R;
import com.yuechu.entity.Category;
import com.yuechu.entity.Employee;
import com.yuechu.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    //分页查询
    @GetMapping("/page")
    public R<IPage> page(int page, int pageSize){
        IPage<Category> iPage = new Page(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(iPage,queryWrapper);
        return R.success(iPage);
    }


    //新增菜品、套餐
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Category category) {

        categoryService.save(category);
        return R.success("保存成功");
    }


    //根据id删除菜品、套餐
    @DeleteMapping
    public R<String> deleteById(Long ids){
        log.info("id={}",ids);
        categoryService.removeById(ids);
        return R.success("delete success!");
    }


}
