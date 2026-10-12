package com.emirhaneren.stream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.emirhaneren.dto.CourierLocationRequest;

@Component
public class CourierLocationStreamer {

	private final RestClient restClient;
	private final Random  random = new Random();
	
	public CourierLocationStreamer() {
		this.restClient= RestClient.builder()
				.baseUrl("http://localhost:8080")
				.build();
	}
	
	private final List<String> courierNames = List.of(
			"Kurye - 1",
			"Kurye - 2",
			"Kurye - 3",
			"Kurye - 4",
			"Kurye - 5",
			"Kurye - 6"
	);
	
	private final List<Double> latitudes=List.of(
			41.0066851,
            41.0070000,
            41.0065000,
            41.0080000,
            41.0055000
    );
	
	private final List<Double> longitudes = List.of(
			28.6552262,
            28.6558150,
            28.6560000,
            28.6570000,
            28.6545000
    );
	
	@Scheduled(fixedDelay = 10000)
	public void streamData() {
		String courierName = courierNames.get(random.nextInt(courierNames.size()));
		double lat = latitudes.get(random.nextInt(latitudes.size()));
		double lng = longitudes.get(random.nextInt(longitudes.size()));
		
		CourierLocationRequest request = new CourierLocationRequest();
		request.setCourierName(courierName);
		request.setLat(lat);
		request.setLng(lng);
		request.setTime(LocalDateTime.now());
		
		try {
			
			System.out.printf(
			        "%n[STREAM] Kurye: %-12s | Lat: %.7f | Lng: %.7f%n",
			        courierName,
			        lat,
			        lng
			);
			
			restClient.post()
			.uri("/api/rest/courier/send-location")
			.body(request)
			.retrieve()
			.toBodilessEntity();
			
			System.out.println("[STREAM] Kurye bilgisi işlendi.");
			
		}catch (Exception e) {
			System.out.println("Konum gönderilemedi: "+ e.getMessage());
		}
	}
}
