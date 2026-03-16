package com.example.crudstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CrudStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrudStoreApplication.class, args);
    }
}
