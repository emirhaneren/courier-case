package com.emirhaneren.config;

import java.io.InputStream;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.emirhaneren.entity.Store;
import com.emirhaneren.repository.StoreRepository;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class StoreDataLoader implements CommandLineRunner {

	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Override
	public void run(String... args) throws Exception {
		
		System.out.println("Data loader çalıştı");
		
		if(storeRepository.count()>0) {
			return;
		}
		
		ClassPathResource resource = new ClassPathResource("jsonData/stores.json");
		
		try (InputStream inputStream = resource.getInputStream()) {
			Store[] stores = objectMapper.readValue(inputStream, Store[].class);
			storeRepository.saveAll(Arrays.asList(stores));
			System.out.println("Mağazalar yüklendi");
		}catch (Exception e) {
			System.out.println("HATA: Mağazalar yüklenemedi");
			System.out.println("Hata Detayı :" + e.getMessage());
			e.printStackTrace();
		}
		
	}

}
