package com.sky.annotation;

import com.sky.enumeration.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autofill {
    //自定义注解中，属性的定义格式是「返回值类型 属性名 ()」，这里的返回值类型就是属性的类型
    //因为不用赋值，所以select不需要这个注解
    OperationType value();
}
