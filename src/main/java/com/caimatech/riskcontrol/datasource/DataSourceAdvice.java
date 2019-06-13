package com.caimatech.riskcontrol.datasource;

import java.lang.reflect.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;

public class DataSourceAdvice  implements MethodBeforeAdvice, AfterReturningAdvice, ThrowsAdvice {

    protected static Logger logger = LoggerFactory.getLogger(DataSourceAdvice.class);

    // 抛出Exception之后被调用
    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        logger.error("出现异常,切换到: master ex = {} ",ex);
        DatabaseContextHolder.setCustomerType("master");
    }
    
    public void afterReturning(Object arg0, Method arg1, Object[] arg2, Object arg3) throws Throwable {
        
    }

    public void before(Method method, Object[] arg, Object target) throws Throwable {
        String className = target.getClass().getName();
        String methodName = method.getName();
        logger.info("切入点: {} 类中 {} 方法" , className , methodName );
        
        if(method.getName().startsWith("add")
                || method.getName().startsWith("create")
                || method.getName().startsWith("save")
                || method.getName().startsWith("edit")
                || method.getName().startsWith("update")
                || method.getName().startsWith("delete")
                || method.getName().startsWith("add")
                || method.getName().startsWith("remove")){
            
            logger.info("切换到: master");
            DatabaseContextHolder.setCustomerType("master");

        }else{
            logger.info("切换到: slave");
            DatabaseContextHolder.setCustomerType("slave");
        }
    }

}
