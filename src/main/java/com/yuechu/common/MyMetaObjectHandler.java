package com.yuechu.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {

        long id = Thread.currentThread().getId();
        log.info("当前线程id为：{}",id);

       metaObject.setValue("createTime",LocalDateTime.now());
       metaObject.setValue("updateTime",LocalDateTime.now());
       metaObject.setValue("createUser",BaseContext.getCurrentId());
       metaObject.setValue("updateUser",BaseContext.getCurrentId());


    }

    @Override
    public void updateFill(MetaObject metaObject) {

        long id = Thread.currentThread().getId();
        log.info("当前线程id为：{}",id);

        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getCurrentId());
    }
}
