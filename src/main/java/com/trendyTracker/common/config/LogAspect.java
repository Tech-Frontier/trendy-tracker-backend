package com.trendyTracker.common.config;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.trendyTracker.common.kafka.KafkaProducer;

@Aspect
@Component
public class LogAspect {
    @Autowired
    private KafkaProducer kafkaProducer;
    private Map<String, Long> requestStartTimes = new HashMap<>();

    // Loggable 어노테이션이 클래스에 적용되어 있는 경우에만
    @Pointcut("@within(Loggable)")
    public void loggableClass(){
    }

    @Before("loggableClass() && execution(* *(..))")
    public void checkMethodStartTime(JoinPoint joinPoint) throws Throwable {
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UUID uuid = (UUID) request.getAttribute("uuid");

        requestStartTimes.put(uuid.toString(), System.currentTimeMillis());
    }


    // API
    @After("loggableClass() && execution(* *(..))")
    public void logAPIMethod(JoinPoint joinPoint) throws Throwable {
    /*
     * 로깅 관련 정보를 kafka, logger 에 기록합니다.
     * 'api_path', 'params', 'duration_time', 'uuid' 
     */
        var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UUID uuid = (UUID) request.getAttribute("uuid");

        List<String> paramNames = extractParamName(joinPoint);
        List<Object> paramValues = extractParamValue(joinPoint, uuid);
        
        // make params
        String inputParms ="";
        for(int i =0; i< paramNames.size(); i++)
            inputParms += assembleParamInfo(paramNames.get(i),paramValues.get(i));
            
        kafkaProducer.sendMessage("logger",request.getRequestURI(), inputParms, uuid.toString());
        logger.info(String.format("\nAPI path: %s \nparams: %s", request.getRequestURI(), inputParms));
        deleteUUIDMap(uuid);
    }


    // Exception 
    @AfterThrowing(pointcut = "loggableClass() && execution(* *(..))", throwing = "ex")
    /*
     * Exception 에 대한 정보를 kafka, logger 에 기록합니다. 
     */
    public void logAPIException(JoinPoint joinPoint, Throwable ex) {
        var logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        var request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        UUID uuid = (UUID) request.getAttribute("uuid");

        List<String> paramNames = extractParamName(joinPoint);
        List<Object> paramValues = extractParamValue(joinPoint, uuid);

        // make params
        String inputParms ="";
        for(int i =0; i< paramNames.size(); i++)
            inputParms += assembleParamInfo(paramNames.get(i),paramValues.get(i));
        inputParms += String.format("\n%s: %s",ex.getClass().getSimpleName(), ex.getMessage());

        kafkaProducer.sendMessage("error",request.getRequestURI(), inputParms, uuid.toString());
        logger.error(String.format("\nAPI path: %s \nparams: %s", request.getRequestURI(), inputParms));
    }

    private List<String> extractParamName(JoinPoint joinPoint) {
    /*
     * HTTP API request Params name 추출
     */
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] methodParams = method.getParameters();
        List<String> params = new ArrayList<>();

        for(int i =0; i< methodParams.length; i++){
            RequestParam param = methodParams[i].getAnnotation(RequestParam.class);
            if(param == null) 
                continue;
            
            params.add(param.name());
        }

        // add custom param 
        params.add("durationTime");
        return params;
    }


    private List<Object> extractParamValue(JoinPoint joinPoint, UUID uuid) {
    /*
     * HTTP API request Params value 추출
     */
        Object[] args = joinPoint.getArgs();
        List<Object> paramValues = new ArrayList<>();

        for(int i=0; i< args.length; i++){
            if(args[i] instanceof RequestFacade || args[i] instanceof ResponseFacade)
                continue;
            
            paramValues.add(args[i]);
        }

        // add custom param
        paramValues.add(System.currentTimeMillis() - requestStartTimes.get(uuid.toString()));
        return paramValues;
    }

    private String assembleParamInfo(String name, Object param) {
    /*
     * HTTP API 요청 Params 조립
     */
        String paramValue = "";
        if (param == null)  return "";
            
        if (param instanceof String[]) {
            String[] arrayParam = (String[]) param;
            for (String value : arrayParam) 
                paramValue += String.format("\n%s: %s",name, value);

        } else if (param instanceof String) 
            paramValue += String.format("\n%s: %s",name, param);

        else if (param instanceof Integer) 
            paramValue += String.format("\n%s: %s",name, param);

        else if (param instanceof Long)
            paramValue += String.format("\n%s: %s ms",name, param);
        

        return paramValue;
    }

    private void deleteUUIDMap(UUID uuid) {
        if(requestStartTimes.get(uuid.toString()) != null)
            requestStartTimes.remove(uuid.toString());
    }
}
