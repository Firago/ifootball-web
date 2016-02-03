package com.mobica.ifootball.repository;

import com.mobica.ifootball.domain.SensorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for the SensorData entity.
 */
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {

    @Query("select sd from SensorData sd where sd.time > :time ")
    List<SensorData> findWhereTimeGreaterThan(@Param("time") ZonedDateTime time);

}
