package com.emirhaneren.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emirhaneren.entity.Courier;
import com.emirhaneren.entity.Store;
import com.emirhaneren.entity.StoreEntrance;

public interface StoreEntranceRepository extends JpaRepository<StoreEntrance, Long>{
	Optional<StoreEntrance> findTopByCourierAndStoreOrderByEntrenceTimeDesc(Courier courier, Store store);
}
