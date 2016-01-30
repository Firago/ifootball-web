package com.mobica.ifootball.repository;

import com.mobica.ifootball.domain.SensorData;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SensorData entity.
 */
public interface SensorDataRepository extends JpaRepository<SensorData,Long> {

}
