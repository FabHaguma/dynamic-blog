package com.haguma.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		// Load environment variables from .env file
		Dotenv.configure().ignoreIfMissing().systemProperties().load();
		SpringApplication.run(BlogApplication.class, args);
	}

}
