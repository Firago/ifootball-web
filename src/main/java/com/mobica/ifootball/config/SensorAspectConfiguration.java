package com.mobica.ifootball.config;

import com.mobica.ifootball.aop.sensor.SensorAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by dmfi on 01/02/2016.
 */
@Configuration
@EnableAspectJAutoProxy
public class SensorAspectConfiguration {

    @Bean
    public SensorAspect sensorAspect() {
        return new SensorAspect();
    }

}
