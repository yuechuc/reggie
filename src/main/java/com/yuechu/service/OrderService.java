package com.yuechu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yuechu.entity.Orders;


public interface OrderService extends IService<Orders> {

    public void submit(Orders orders);
}
