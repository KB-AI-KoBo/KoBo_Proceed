package com.kb.kobo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableTransactionManagement
public class KoBoApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(KoBoApplication.class, args);
    }

}