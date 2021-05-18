package com.singhrepo.covid19tracker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.singhrepo.covid19tracker.service.CovidDataService;

@SpringBootApplication
@EnableScheduling
public class Covid19TrackerApplication implements CommandLineRunner{

	@Autowired
	CovidDataService dataservice;
	
	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(Covid19TrackerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*
		 * String output =dataservice.fetchVirusData(); System.out.println(output);
		 */
	}

}
