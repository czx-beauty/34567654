package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.Autofill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {


    Page<Employee> pageQ(EmployeePageQueryDTO employeePageQueryDTO);


    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    @Autofill(value = OperationType.INSERT)
    void newEmployee(@Param("employee") Employee employee1);

    @Autofill(value = OperationType.UPDATE)
    void update(Employee e);

    EmployeeDTO searchemp(Integer id);
}
