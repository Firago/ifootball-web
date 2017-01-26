package com.mobica.ifootball.repository;

import com.mobica.ifootball.domain.Parameter;
import com.mobica.ifootball.domain.enumeration.ParameterKey;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the Parameter entity.
 */
public interface ParameterRepository extends JpaRepository<Parameter,Long> {

    Parameter findByKey(ParameterKey key);
}
