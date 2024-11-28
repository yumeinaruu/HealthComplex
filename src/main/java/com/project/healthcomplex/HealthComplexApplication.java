package com.project.healthcomplex;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Health complex",
        description = "Health complex",
        contact = @Contact(name = "Студент",
                url = "https://github.com/",
                email = "email")
))
@SpringBootApplication
public class HealthComplexApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthComplexApplication.class, args);
    }

}
