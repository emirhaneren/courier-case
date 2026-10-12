package com.emirhaneren.service;

import com.emirhaneren.dto.CourierDistanceResponse;
import com.emirhaneren.dto.CourierLocationRequest;
import com.emirhaneren.dto.CourierLocationResponse;

public interface ICourierLocationService {
	CourierLocationResponse processLocation(CourierLocationRequest request);
	CourierDistanceResponse getTotalTravelDistance(Long id);
}
