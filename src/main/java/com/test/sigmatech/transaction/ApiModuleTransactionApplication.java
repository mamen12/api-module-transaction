package com.test.sigmatech.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.test.sigmatech.transaction", "com.commons.beans"})
public class ApiModuleTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiModuleTransactionApplication.class, args);
	}

}
