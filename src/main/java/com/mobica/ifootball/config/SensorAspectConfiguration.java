package com.mobica.ifootball.config;

import com.mobica.ifootball.aop.sensor.SensorAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;

/**
 * Created by dmfi on 01/02/2016.
 */
@Configuration
@EnableAspectJAutoProxy
public class SensorAspectConfiguration {

    @Bean
    @Profile(Constants.SPRING_PROFILE_DEVELOPMENT)
    public SensorAspect sensorAspect() {
        return new SensorAspect();
    }

}
