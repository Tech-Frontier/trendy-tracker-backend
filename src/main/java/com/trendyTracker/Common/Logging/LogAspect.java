package com.trendyTracker.Common.Logging;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.catalina.connector.RequestFacade;
import org.apache.catalina.connector.ResponseFacade;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trendyTracker.Adaptors.MessagingSystem.KafkaProducer;

import org.springframework.web.bind.annotation.RequestParam;


@Aspect
@Component
public class LogAspect {
    @Autowired
    private KafkaProducer kafkaProducer;
    private Map<String, Long> requestStartTimes = new HashMap<>();

    @Pointcut("@within(Loggable)")
    public void loggableClass() {
    }

    @Before("loggableClass() && execution(* *(..))")
    public void checkMethodStartTime(JoinPoint joinPoint) throws Throwable {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UUID uuid = (UUID) request.getAttribute("uuid");

        requestStartTimes.put(uuid.toString(), System.currentTimeMillis());
    }

    @After("loggableClass() && execution(* *(..))")
    public void logAPIMethod(JoinPoint joinPoint) throws Throwable {
        log(joinPoint, null);
    }

    @AfterThrowing(pointcut = "loggableClass() && execution(* *(..))", throwing = "ex")
    public void logAPIException(JoinPoint joinPoint, Throwable ex) {
        log(joinPoint, ex);
    }

    private void log(JoinPoint joinPoint, Throwable ex) {
        var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UUID uuid = (UUID) request.getAttribute("uuid");

        List<String> paramNames = extractParamName(joinPoint);
        List<Object> paramValues = extractParamValue(joinPoint, uuid);

        String inputParams = assembleParams(paramNames, paramValues);

        if (ex != null) {
            inputParams += String.format("\n%s: %s", ex.getClass().getSimpleName(), ex.getMessage());
            kafkaProducer.sendMessage("error", request.getRequestURI(), inputParams, uuid.toString());
            logger.error(String.format("\nAPI path: %s \nparams: %s", request.getRequestURI(), inputParams));
        } else {
            kafkaProducer.sendMessage("logger", request.getRequestURI(), inputParams, uuid.toString());
            logger.info(String.format("\nAPI path: %s \nparams: %s", request.getRequestURI(), inputParams));
        }

        deleteUUIDMap(uuid);
    }

    private List<String> extractParamName(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] methodParams = method.getParameters();
        List<String> params = new ArrayList<>();

        for (Parameter param : methodParams) {
            RequestParam requestParam = param.getAnnotation(RequestParam.class);
            if (requestParam != null) {
                params.add(requestParam.name());
            }
        }

        params.add("durationTime");
        return params;
    }

    private List<Object> extractParamValue(JoinPoint joinPoint, UUID uuid) {
        Object[] args = joinPoint.getArgs();
        List<Object> paramValues = new ArrayList<>();

        for (Object arg : args) {
            if (!(arg instanceof RequestFacade || arg instanceof ResponseFacade)) {
                paramValues.add(arg);
            }
        }

        paramValues.add(System.currentTimeMillis() - requestStartTimes.getOrDefault(uuid.toString(), 0L)+ "ms");
        return paramValues;
    }

    private String assembleParams(List<String> paramNames, List<Object> paramValues) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < paramNames.size(); i++) {
            builder.append(String.format("\n%s: %s", paramNames.get(i), paramValues.get(i)));
        }
        return builder.toString();
    }

    private void deleteUUIDMap(UUID uuid) {
        requestStartTimes.remove(uuid.toString());
    }
}
