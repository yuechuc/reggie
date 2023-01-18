package com.yuechu.controller;

import com.yuechu.common.R;
import com.yuechu.entity.Orders;
import com.yuechu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Autowired
    private OrderService ordersService;



    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("order={}",orders.toString());

        ordersService.submit(orders);

        return R.success("success");
    }
}
