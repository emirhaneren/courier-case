package com.emirhaneren.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emirhaneren.entity.Courier;
import com.emirhaneren.entity.LocationRecord;

public interface LocationRecordRepository extends JpaRepository<LocationRecord, Long>{
	Optional<LocationRecord> findTopByCourierOrderByTimeDesc(Courier courier);

    List<LocationRecord> findByCourierOrderByTimeAsc(Courier courier);
}
