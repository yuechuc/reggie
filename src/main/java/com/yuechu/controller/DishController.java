package com.yuechu.controller;


import com.yuechu.common.R;
import com.yuechu.dto.DishDto;
import com.yuechu.service.DishFlavorService;
import com.yuechu.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
//        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("success");
    }

}
