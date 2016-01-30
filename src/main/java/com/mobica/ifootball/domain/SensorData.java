package com.mobica.ifootball.domain;

import java.time.ZonedDateTime;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A SensorData.
 */
@Entity
@Table(name = "sensor_data")
public class SensorData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private ZonedDateTime time;
    
    @NotNull
    @Min(value = 0)
    @Column(name = "value", nullable = false)
    private Float value;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SensorData sensorData = (SensorData) o;
        if(sensorData.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, sensorData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "SensorData{" +
            "id=" + id +
            ", time='" + time + "'" +
            ", value='" + value + "'" +
            '}';
    }
}
