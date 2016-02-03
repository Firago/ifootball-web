package com.mobica.ifootball.domain;

import com.mobica.ifootball.domain.enumeration.Status;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A StatusHistory.
 */
@Entity
@Table(name = "status_history")
public class StatusHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    public StatusHistory() {
        
    }

    public StatusHistory(ZonedDateTime time, Status status) {
        this.time = time;
        this.status = status;
    }

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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusHistory statusHistory = (StatusHistory) o;
        if (statusHistory.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, statusHistory.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "StatusHistory{" +
            "id=" + id +
            ", time='" + time + "'" +
            ", status='" + status + "'" +
            '}';
    }
}
