package com.example.demo;

import com.example.demo.User.User;
import com.example.demo.User.UserRepo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	// @Bean
	// CommandLineRunner commandLineRunner(UserRepo userRepo) {
	// return args -> {
	// User ye = new User(1, "bruce", "bruce", "bruce@gmail.com");
	// userRepo.save(ye);
	// };
	// }

}
