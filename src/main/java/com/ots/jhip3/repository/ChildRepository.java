package com.ots.jhip3.repository;

import com.ots.jhip3.domain.Child;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Child entity.
 */
public interface ChildRepository extends JpaRepository<Child,Long> {
	List<Child> findAllByCwcase(Long cwcase);
}
