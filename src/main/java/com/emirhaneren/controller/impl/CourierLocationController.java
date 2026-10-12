package com.emirhaneren.controller.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emirhaneren.controller.ICourierLocationController;
import com.emirhaneren.dto.CourierDistanceResponse;
import com.emirhaneren.dto.CourierLocationRequest;
import com.emirhaneren.dto.CourierLocationResponse;
import com.emirhaneren.service.impl.CourierLocationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name="Courier Controller",description = "Kurye konum ve mesafe işlemleri")
@RestController
@RequestMapping("/api/rest/courier")
public class CourierLocationController implements ICourierLocationController{

	@Autowired
	private CourierLocationService courierLocationService;
	
	@Operation(
			summary = "Kurye konumunu gönder",
			description = "Kurye konumu kaydeder ve mağaza giriş kurallarını uygular")
	@PostMapping("/send-location")
	public ResponseEntity<CourierLocationResponse> receiveLocation(@Valid @RequestBody CourierLocationRequest request){
		CourierLocationResponse response = courierLocationService.processLocation(request);
		return ResponseEntity.ok(response);
	}
	
	@Operation(
			summary = "Toplam mesafeyi getirir",
			description = "Belirtilen kuryenin toplam katettiği mesafeyi döndürür")
	@GetMapping("/total-distance/{id}")
	public ResponseEntity<CourierDistanceResponse> getTotalTravelDistance(@PathVariable(name = "id") Long id){
		CourierDistanceResponse response = courierLocationService.getTotalTravelDistance(id);
		 return ResponseEntity.ok(response);
	}
}
