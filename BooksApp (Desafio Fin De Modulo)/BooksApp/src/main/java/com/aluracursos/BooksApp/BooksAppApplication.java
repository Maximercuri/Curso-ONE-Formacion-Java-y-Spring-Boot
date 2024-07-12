package com.aluracursos.BooksApp;

import com.aluracursos.BooksApp.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BooksAppApplication implements CommandLineRunner {

	public static void main(String[] args) {

		SpringApplication.run(BooksAppApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();

		principal.ejecutarPrograma();

	}

}
