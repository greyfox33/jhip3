package com.ots.jhip3.repository;

import com.ots.jhip3.domain.Hearing;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Hearing entity.
 */
public interface HearingRepository extends JpaRepository<Hearing,Long> {
	List<Hearing> findAllByCwcase(Long cwcase);
}
