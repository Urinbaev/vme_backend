package ru.sibintek.vme;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class VmeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VmeApplication.class, args);
    }

}
