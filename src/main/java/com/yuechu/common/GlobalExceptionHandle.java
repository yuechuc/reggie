package com.yuechu.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandle {

    //处理新增员工异常
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandle(SQLIntegrityConstraintViolationException exception) {
        log.info(exception.getMessage());
        String msg = null;
        if (exception.getMessage().contains("Duplicate entry")) {
//            splid  根据空格分组
            String[] split = exception.getMessage().split(" ");

            msg = split[2] + "已存在，添加失败！";
            return R.error(msg);

        }
        return R.error("未知错误！");
    }


}
