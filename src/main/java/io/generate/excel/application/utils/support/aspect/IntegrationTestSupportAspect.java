package io.generate.excel.application.utils.support.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Aspect
@Component
@Slf4j
public class IntegrationTestSupportAspect {

    @Around(value = "@annotation(io.generate.excel.application.utils.support.annotation.IntegrationTestSupport)")
    public Object checkMemory(ProceedingJoinPoint joinPoint) throws Throwable {
        long beforeMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);

        Object result = joinPoint.proceed();

        long afterMemory = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        long usedMemory = afterMemory - beforeMemory;

        log.info("[report] [IntegrationTestSupport] : Before Memory : {}, After Memory : {}, Used Memory : {}", beforeMemory, afterMemory, usedMemory);

        Field[] declaredFields = result.getClass().getDeclaredFields();

        for (Field declaredField : declaredFields) {
            if (!declaredField.canAccess(result)) {
                declaredField.setAccessible(true);
            }

            String fieldName = declaredField.getName();

            switch (fieldName) {
                case "before" -> declaredField.set(result, beforeMemory);
                case "after" -> declaredField.set(result, afterMemory);
                case "used" -> declaredField.set(result, usedMemory);
            }
        }

        return result;
    }
}
