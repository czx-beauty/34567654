package com.sky.service;

import com.github.pagehelper.PageHelper;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {
    PageResult pageQuery(DishPageQueryDTO dto);
}
