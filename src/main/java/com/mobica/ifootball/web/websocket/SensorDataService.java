package com.mobica.ifootball.web.websocket;

import com.mobica.ifootball.domain.SensorData;
import com.mobica.ifootball.web.websocket.dto.SensorDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;

/**
 * Created by dmfi on 31/01/2016.
 */
@Controller
public class SensorDataService {

    private static final Logger log = LoggerFactory.getLogger(SensorDataService.class);

    @Inject
    SimpMessageSendingOperations messagingTemplate;

    public void updateChart(SensorData sensorData) {
        SensorDataDTO sensorDataDTO = new SensorDataDTO();
        sensorDataDTO.setTime(sensorData.getTime());
        sensorDataDTO.setValue(sensorData.getValue());
        log.trace("Updating chart with {}", sensorDataDTO);
        messagingTemplate.convertAndSend("/sensor/chart", sensorDataDTO);
    }
}
