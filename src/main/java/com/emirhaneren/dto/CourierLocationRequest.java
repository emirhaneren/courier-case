package com.emirhaneren.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CourierLocationRequest {
	private LocalDateTime time;
	
	@NotBlank(message = "Kurye adı dolu olmalıdır.")
	private String courierName;
	
	@NotNull(message = "Enlem bilgisi (lat) dolu olmalıdır.")
	@DecimalMin(value = "-90.0", message = "Enlem bilgisi (lat) -90 ile 90 arasında olmalıdır")
    @DecimalMax(value = "90.0", message = "Enlem bilgisi (lat) -90 ile 90 arasında olmalıdır")
	private double lat;
	
	@NotNull(message = "Boylam bilgisi (lng) dolu olmalıdır.")
	@DecimalMin(value = "-90.0", message = "Boylam bilgisi (lng) -90 ile 90 arasında olmalıdır")
    @DecimalMax(value = "90.0", message = "Boylam bilgisi (lng) -90 ile 90 arasında olmalıdır")
	private double lng;
}
