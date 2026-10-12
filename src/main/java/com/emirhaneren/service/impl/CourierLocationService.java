package com.emirhaneren.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.emirhaneren.constants.StoreConstants;
import com.emirhaneren.dto.CourierDistanceResponse;
import com.emirhaneren.dto.CourierLocationRequest;
import com.emirhaneren.dto.CourierLocationResponse;
import com.emirhaneren.entity.Courier;
import com.emirhaneren.entity.LocationRecord;
import com.emirhaneren.entity.Store;
import com.emirhaneren.entity.StoreEntrance;
import com.emirhaneren.repository.CourierRepository;
import com.emirhaneren.repository.LocationRecordRepository;
import com.emirhaneren.repository.StoreEntranceRepository;
import com.emirhaneren.repository.StoreRepository;
import com.emirhaneren.service.ICourierLocationService;

@Service
public class CourierLocationService implements ICourierLocationService{
		
	@Autowired
	private CourierRepository courierRepository;
	@Autowired
	private LocationRecordRepository locationRecordRepository;
	@Autowired
	private StoreRepository storeRepository;
	@Autowired
	private StoreEntranceRepository storeEntranceRepository;
	
	@Transactional
	public CourierLocationResponse processLocation(CourierLocationRequest request) {
		
		//Gelen istekteki kuryeyi kontrol edelim. Eğer yoksa yeni kurye oluşturup tabloya kaydedelim.
		Optional<Courier> courier = courierRepository.findByName(request.getCourierName());
		if(courier.isEmpty()) {
			Courier newCourier = new Courier();
			newCourier.setName(request.getCourierName());
			newCourier = courierRepository.save(newCourier);

			courier = Optional.of(newCourier);
		}
		
		LocalDateTime currentTime =request.getTime();
		
		//Karşılaştırma yapacağımız için gelen konumdan bir önceki konumu alıyoruz.
		Optional<LocationRecord> previousLocationRecord = locationRecordRepository.findTopByCourierOrderByTimeDesc(courier.get());
		
		//İstekten gelen son konum kaydını tabloya kaydediyoruz.
		LocationRecord currLocationRecord = new LocationRecord();
		currLocationRecord.setTime(currentTime);
		currLocationRecord.setLat(request.getLat());
		currLocationRecord.setLng(request.getLng());
		currLocationRecord.setCourier(courier.get());
		locationRecordRepository.save(currLocationRecord);
		
		//Tüm mağazalar için mesafe kontrolü yapacağız.
		List <Store> stores = storeRepository.findAll();
		for (Store store : stores) {
			double currentDistance = calculateDistance(request.getLat(),request.getLng(),store.getLat(),store.getLng());
			
			boolean isCurrentlyInside = currentDistance<= StoreConstants.STORE_RADIUS_METERS;
			boolean isPreviouslyInside = false;
			
			//kurye önceden de mağaza sınırları içerisinde miydi ? kontrolü yapacağız
			if(!previousLocationRecord.isEmpty()) {
				double previouslyDistance = calculateDistance(
						previousLocationRecord.get().getLat(),
						previousLocationRecord.get().getLng(), 
						store.getLat(), 
						store.getLng());
				
				isPreviouslyInside = previouslyDistance<=StoreConstants.STORE_RADIUS_METERS;
			}
			
			// Zaten mağazanın 100 metre içindeyse loglamadan geçelim.
			if(!isCurrentlyInside || isPreviouslyInside) {
				
				if(!isCurrentlyInside) {
					System.out.println("[ATLANDI] Kurye mağazanın 100 metre dışında: " + courier.get().getName() + " - " + store.getName()+" (100m kuralı)");
				} else {
					System.out.println("[ATLANDI] Kurye zaten mağazanın 100 metre içinde: " + courier.get().getName() + " - " + store.getName()+" (100m kuralı)");
				}
				
				continue;
			}
			
			//son girişinden bu yana 60 saniye geçti mi ?
			Optional<StoreEntrance> lastStoreEntryOptional = storeEntranceRepository.findTopByCourierAndStoreOrderByEntrenceTimeDesc(courier.get(), store);
			if(lastStoreEntryOptional.isPresent()) {
				long seconds = Duration.between(lastStoreEntryOptional.get().getEntrenceTime(), currentTime).getSeconds();
				if(seconds<0) {
					continue;
				}
				if(seconds<= StoreConstants.ENTRANCE_COOLDOWN_SECONDS) {
				    System.out.println("[ATLANDI] Kurye son girişinden itibaren " + seconds + " saniye geçtiği için yeniden loglanmadı: " + courier.get().getName() + " - " + store.getName() + " (60sn kuralı)");
					continue;
				}
			}
			
			StoreEntrance storeEntrance = new StoreEntrance();
			storeEntrance.setCourier(courier.get());
			storeEntrance.setStore(store);
			storeEntrance.setEntrenceTime(currentTime);
			storeEntrance.setDistanceMeters(currentDistance);
			storeEntranceRepository.save(storeEntrance);
			
			System.out.printf(
			        "[MAĞAZA GİRİŞİ] Kurye: %-12s | Mağaza: %-25s | Mesafe: %6.2f metre | Zaman: %s%n",
			        courier.get().getName(),
			        store.getName(),
			        currentDistance,
			        currentTime
			);
		}
		
		return new CourierLocationResponse(
		        true,
		        "success"
		);
	}
	
	public CourierDistanceResponse getTotalTravelDistance(Long id) {
		
		Optional<Courier> courierOptional = courierRepository.findById(id);
		if(courierOptional.isEmpty()) {
			return new CourierDistanceResponse(
		            false,
		            "Kurye bulunamadı: " + id,
		            0.0
		        );
		}
		
		List<LocationRecord> locationRecords = locationRecordRepository.findByCourierOrderByTimeAsc(courierOptional.get());
		double totalDistance = 0.0;
		
		for (int i = 1; i<locationRecords.size();i++) {
			LocationRecord previousLocationRecord= locationRecords.get(i-1);
			LocationRecord currentLocationRecord = locationRecords.get(i);
			
			totalDistance += calculateDistance(
					previousLocationRecord.getLat(),
					previousLocationRecord.getLng(), 
					currentLocationRecord.getLat(), 
					currentLocationRecord.getLng());
		}
		
		return new CourierDistanceResponse(
	            true,
	            "success",
	            totalDistance
	        );
	}
	
	//Haversine Formülü kullanıldı mesafe hesaplamak için.
	private double calculateDistance(double courierLat, double courierLng, double storeLat, double storeLng) {
				
		double latDistance=Math.toRadians(storeLat-courierLat);
		double lngDistance = Math.toRadians(storeLng-courierLng);
		
		double haversineValue =Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
		        + Math.cos(Math.toRadians(courierLat))
		        * Math.cos(Math.toRadians(storeLat))
		        * Math.sin(lngDistance / 2)
		        * Math.sin(lngDistance / 2);
		haversineValue = Math.max(0.0, Math.min(1.0, haversineValue));
		double centralAngle = 2* Math.atan2(
				Math.sqrt(haversineValue),Math.sqrt(1-haversineValue));
		
		return StoreConstants.EARTH_RADIUS_METERS * centralAngle;
	}
}
