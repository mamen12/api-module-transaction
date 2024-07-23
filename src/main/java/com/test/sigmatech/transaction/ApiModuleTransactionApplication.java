package com.test.sigmatech.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan({"com.test.sigmatech.transaction", "com.commons.beans"})
@EnableScheduling
public class ApiModuleTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiModuleTransactionApplication.class, args);
	}

}
