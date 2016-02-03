package com.mobica.ifootball.aop.sensor;

import com.mobica.ifootball.domain.SensorData;
import com.mobica.ifootball.web.websocket.SensorDataService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Arrays;

/**
 * Created by dmfi on 01/02/2016.
 */
@Aspect
public class SensorAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Inject
    private SensorDataService sensorDataService;

    @Pointcut("execution(public * com.mobica.ifootball.web.rest.SensorDataResource.createSensorData(..))")
    public void updateChartPointcut() {
    }

    @Before("updateChartPointcut()")
    public void updateChartAround(JoinPoint joinPoint) throws Throwable {
        log.debug("Enter: {}.{}() with argument[s] = {}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
        Object[] args = joinPoint.getArgs();
        sensorDataService.updateChart((SensorData) args[0]);
    }
}
