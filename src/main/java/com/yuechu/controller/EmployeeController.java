package com.yuechu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yuechu.common.R;
import com.yuechu.entity.Employee;
import com.yuechu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1. 把页面提交的密码用md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2. 根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //3. 没有查询到返回用户不存在
        if (emp == null) {
            return R.error("用户不存在");
        }

        //4. 密码比对，错误 ->密码错误   正确—>检查账号状态
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误");
        }

        //5.  查询账号状态 status=1  ==> 正常 登陆成功   status=0 ==》返回账号已禁用
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        //6. 登陆成功，把员工id存入session并返回登陆成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }


}
