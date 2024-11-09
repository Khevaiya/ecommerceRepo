package com.example.e_commerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;



@SpringBootApplication
//@ComponentScan(basePackages = "com.example.e_commerce.controller")  // Make sure it's scanning the correct package
public class ECommerceApplication {

	public static void main(String[] args) {

		SpringApplication.run(ECommerceApplication.class, args);

	}

}
