package be.abis.exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:my.properties")
public class Exercise43Application {

    public static void main(String[] args) {
        SpringApplication.run(Exercise43Application.class, args);
    }

}
