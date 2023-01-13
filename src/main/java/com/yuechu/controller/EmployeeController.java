package com.yuechu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuechu.common.R;
import com.yuechu.entity.Employee;
import com.yuechu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    //登陆功能
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
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }


    //登出功能
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //1. 清理Session中保存的员工id
        request.getSession().removeAttribute("employee");

        return R.success("退出成功！");
    }


    //添加员工
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
//        初始化密码为‘123456’，并使用md5加密储存
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());

        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));

        employeeService.save(employee);
        return R.success("保存成功");
    }


    //员工分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        Page page1 = new Page(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(page1, queryWrapper);
        return R.success(page1);
    }

    //禁用员工账号功能
    @PutMapping
    public R<String> banAccount(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);

        return R.success("员工信息修改成功！");
    }
}
