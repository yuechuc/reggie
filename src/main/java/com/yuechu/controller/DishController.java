package com.yuechu.controller;


import com.yuechu.service.DishFlavorService;
import com.yuechu.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishService dishService;

}
