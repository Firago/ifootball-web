package com.mobica.ifootball.service;

import com.mobica.ifootball.domain.SensorData;
import com.mobica.ifootball.domain.StatusHistory;
import com.mobica.ifootball.domain.enumeration.Status;
import com.mobica.ifootball.repository.SensorDataRepository;
import com.mobica.ifootball.repository.StatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@Transactional
public class StatusUpdateService {

    private final Logger log = LoggerFactory.getLogger(StatusUpdateService.class);

    @Inject
    private SensorDataRepository sensorDataRepository;
    @Inject
    private StatusHistoryRepository statusHistoryRepository;
    @Inject
    private SimpMessageSendingOperations messagingTemplate;

    @Scheduled(fixedRate = 5000)
    public void updateStatus() {
        List<StatusHistory> statusHistory = statusHistoryRepository.findTop1ByOrderByTimeDesc();
        StatusHistory entry = statusHistory.size() > 0 ? statusHistory.get(0) : null;
        Status currentStatus = entry == null ? null : entry.getStatus();
        log.debug("Current status is {}", currentStatus);

        ZonedDateTime now = ZonedDateTime.now();

        List<SensorData> sensorDataList = sensorDataRepository.findWhereTimeGreaterThan(now.minusSeconds(10));
        log.debug("Sensor data count during last 10 seconds - {}", sensorDataList.size());
        if (sensorDataList.isEmpty() && !Status.UNAVAILABLE.equals(currentStatus)) {
            changeStatus(now, Status.UNAVAILABLE);
        } else {
            analyseSensorData(currentStatus, now, sensorDataList);
        }
    }

    private void analyseSensorData(Status currentStatus, ZonedDateTime now, List<SensorData> sensorDataList) {
        final int[] suspicions = {0};
        sensorDataList.stream().filter(sensorData -> sensorData.getValue() > 0.2).forEach(sensorData -> {
            suspicions[0]++;
        });
        log.debug("Number of high amplitude vibrations - {}", suspicions[0]);
        double occupied = (suspicions[0] * 100.0) / sensorDataList.size();
        log.debug("Vibrations took {}% of time during last 10 seconds", occupied);
        if (occupied > 20 && !Status.OCCUPIED.equals(currentStatus)) {
            changeStatus(now, Status.OCCUPIED);
        } else if (occupied <= 20 && !Status.FREE.equals(currentStatus)) {
            changeStatus(now, Status.FREE);
        }
    }

    private void changeStatus(ZonedDateTime time, Status status) {
        log.info("Changing status to {}", status);
        StatusHistory statusHistory = new StatusHistory(time, status);
        statusHistoryRepository.saveAndFlush(statusHistory);
        messagingTemplate.convertAndSend("/table/status", statusHistory);
    }

}
