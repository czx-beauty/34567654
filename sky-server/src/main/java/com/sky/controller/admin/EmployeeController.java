package com.sky.controller.admin;

import com.github.pagehelper.PageInfo;
import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result<Employee> newEmployee(@RequestBody EmployeeDTO employee) {
        log.info("新增员工{}", employee);
        employeeService.newEmployee(employee);

        return Result.success() ;
    }

    @GetMapping("/page")
    @ApiOperation("分页查询员工")
    public Result<PageResult> pageEmployee(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("分页查询员工{}", employeePageQueryDTO);
        PageResult p=employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(p);
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工")
    public Result  updateStatus(@PathVariable Integer status,Long id) {
        employeeService.setstatus(status,id);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("员工查询")
    public Result<EmployeeDTO> searchemp(@PathVariable Integer id) {
        EmployeeDTO e= employeeService.searchemp(id);
        return Result.success(e);
    }

    @PutMapping
    @ApiOperation("修改员工信息")
    public Result  updateEmployee(@RequestBody EmployeeDTO employee) {
        employeeService.updateemp(employee);
        return Result.success();
    }
}
