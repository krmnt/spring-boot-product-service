package com.kierangelo.springbootproductservice.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class LoggingAdvice {

    @Autowired
    private KafkaTemplate<String, Object> template;

    Logger log = LoggerFactory.getLogger(LoggingAdvice.class);
    //private String topic = "topic-product";
    private String topic = "TestTopic";

    @Pointcut(value="execution(* com.kierangelo.springbootproductservice.controller.ProductController.*(..) )")
    public void myPointcut() {
        //empty

    }

    @Around("myPointcut()")
    public Object applicationLogger(ProceedingJoinPoint pjp) throws Throwable {
        ObjectMapper mapper = new ObjectMapper();
        String methodName = pjp.getSignature().getName();
        String className = pjp.getTarget().getClass().toString();
        Object[] array = pjp.getArgs();
//        log.info("Method invoked " + className + " : " + methodName + "()" + "Arguments : "
//                + mapper.writeValueAsString(array));
        template.send(topic, "Method invoked " + className + " : " + methodName + "()" + "Arguments : "
                + mapper.writeValueAsString(array));
        Object object = pjp.proceed();
//        log.info(className + " : " + methodName + "()" + "Response : "
//                + mapper.writeValueAsString(object));
        template.send(topic, className + " : " + methodName + "()" + "Response : "
                + mapper.writeValueAsString(object));
        return object;
    }

}