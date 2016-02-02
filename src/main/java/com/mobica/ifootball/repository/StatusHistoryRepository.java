package com.mobica.ifootball.repository;

import com.mobica.ifootball.domain.StatusHistory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the StatusHistory entity.
 */
public interface StatusHistoryRepository extends JpaRepository<StatusHistory,Long> {

}