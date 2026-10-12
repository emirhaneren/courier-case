package com.emirhaneren.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emirhaneren.entity.Courier;

public interface CourierRepository extends JpaRepository<Courier, Long>{
	Optional<Courier> findByName(String name);
}
