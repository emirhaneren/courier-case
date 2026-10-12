package com.emirhaneren.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import com.emirhaneren.dto.CourierDistanceResponse;
import com.emirhaneren.dto.CourierLocationRequest;
import com.emirhaneren.dto.CourierLocationResponse;

public interface ICourierLocationController {
	ResponseEntity<CourierLocationResponse> receiveLocation(@RequestBody CourierLocationRequest request);
    ResponseEntity<CourierDistanceResponse> getTotalTravelDistance(Long id);
}
