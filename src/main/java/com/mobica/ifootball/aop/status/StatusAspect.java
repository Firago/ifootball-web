package com.mobica.ifootball.aop.status;

import com.mobica.ifootball.domain.StatusHistory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Created by dmfi on 08/02/2016.
 */
@Aspect
public class StatusAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    @Pointcut("execution(public * com.mobica.ifootball.repository.StatusHistoryRepository.save(..))" +
        "|| execution(public * com.mobica.ifootball.repository.StatusHistoryRepository.saveAndFlush(..))")
    public void updateStatusPointcut() {
    }

    @After("updateStatusPointcut()")
    public void updateStatusAround(JoinPoint joinPoint) throws Throwable {
        log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        Object[] args = joinPoint.getArgs();
        StatusHistory statusHistory = (StatusHistory) args[0];
        messagingTemplate.convertAndSend("/table/status", statusHistory);
    }

}
