package com.emirhaneren.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourierDistanceResponse {
	private boolean isSuccess;
	private String returnMessageString;
    private double totalDistanceMeters;
}
