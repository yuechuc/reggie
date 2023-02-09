package com.yuechu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuechu.common.R;
import com.yuechu.entity.Employee;
import com.yuechu.entity.Orders;
import com.yuechu.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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

    //获取订单
    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize) {
        log.info("page={},pageSize={},name={}", page, pageSize);
        Page page1 = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Orders::getOrderTime);
        ordersService.page(page1, queryWrapper);
        return R.success(page1);
    }
}
