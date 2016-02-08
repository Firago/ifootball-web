package com.mobica.ifootball.config;

import com.mobica.ifootball.aop.status.StatusAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Created by dmfi on 08/02/2016.
 */
@Configuration
@EnableAspectJAutoProxy
public class StatusAspectConfiguration {

    @Bean
    public StatusAspect statusAspect() {
        return new StatusAspect();
    }

}
