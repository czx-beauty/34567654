package com.sky.aspect;

import com.sky.annotation.Autofill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component//Bean交给Spring容器管理
@Aspect//切面类
@Slf4j
public class AutoFillAspect {

//    execution(
//    *          // 返回值类型：* 代表任意返回值（void、String、实体类等）
//    com.sky.mapper.*  // 包名：com.sky.mapper 下的所有类（* 代表任意类）
//            .*         // 方法名：* 代表任意方法名（insert、update、delete等）
//            (..)       // 参数：(..) 代表任意参数（无参、单参、多参都匹配）

    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.Autofill)")//切入点
    public void autoFillPointcut() {}

//            1. 环绕通知@Around（执行前逻辑）→
//            2. 前置通知（@Before）→
//            3. 目标方法执行 →
//            - 若正常执行：后置返回通知（@AfterReturning）→
//            - 若抛异常：异常通知（@AfterThrowing）→
//            4. 最终通知（@After）→
//            5. 环绕通知（执行后逻辑）
    @Before("autoFillPointcut()")
    public void doBefore(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //获取到当前被拦截的方法上的注解对象
        log.info("doBefore: {},开始进行公共字段填充", joinPoint.getSignature());
        // 步骤1：从JoinPoint中获取方法签名（MethodSignature）
        // 注意：JoinPoint.getSignature() 返回的是Signature，需要强转为MethodSignature（因为拦截的是方法）
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        // 步骤2：从方法签名中获取目标方法的Method对象
        Method method = methodSignature.getMethod();

        // 步骤3：从Method对象中获取@AutoFill注解
        // getAnnotation()：如果方法上有该注解，返回注解对象；无则返回null（但切点已过滤，所以一定不为null）
        Autofill autoFillAnnotation = method.getAnnotation(Autofill.class);
        OperationType value = autoFillAnnotation.value();//获取数据库处理类型

        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0) {
            return;
        }
        Object entity = args[0];//获取到当前被拦截的方法的第一个参数，即实体对象

        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
         if(OperationType.INSERT.equals(value)) {
             // 获取setCreateTime方法对象
             Method setCreateTime = entity.getClass().getDeclaredMethod("setCreateTime", LocalDateTime.class);
             setCreateTime.invoke(entity, now);
             // 获取setCreateUser方法对象
             Method setCreateUser = entity.getClass().getDeclaredMethod("setCreateUser", Long.class);
             setCreateUser.invoke(entity, currentId);
             // 获取setUpdateTime方法对象
             Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
             setUpdateTime.invoke(entity, now);
             Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
             setUpdateUser.invoke(entity, currentId);
         }else if(OperationType.UPDATE.equals(value)) {
             Method setUpdateTime = entity.getClass().getDeclaredMethod("setUpdateTime", LocalDateTime.class);
             setUpdateTime.invoke(entity, now);
             Method setUpdateUser = entity.getClass().getDeclaredMethod("setUpdateUser", Long.class);
             setUpdateUser.invoke(entity, currentId);
         }

    }

}
