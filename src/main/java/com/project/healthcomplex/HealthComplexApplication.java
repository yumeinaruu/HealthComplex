package com.project.healthcomplex;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(
        title = "Health complex",
        description = "Health complexn",
        contact = @Contact(name = "Студент имя",
                url = "https://github.com/",
                email = "почта")
))
@SpringBootApplication
public class HealthComplexApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthComplexApplication.class, args);
    }

}
