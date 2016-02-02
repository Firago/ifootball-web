package com.mobica.ifootball.web.websocket.dto;

import java.time.ZonedDateTime;

/**
 * Created by dmfi on 01/02/2016.
 */
public class SensorDataDTO {

    private ZonedDateTime time;

    private Float value;

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "SensorDataDTO{" +
            "time=" + time +
            ", value=" + value +
            '}';
    }
}
