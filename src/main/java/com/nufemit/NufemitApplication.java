package com.nufemit;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info = @Info(title = "NUFEMIT API", description = "NUFEMIT API available endpoints", version = "1.0", contact = @Contact(url = "https://github.com/fedecostan/nufemit-back", name = "Federico Costantino Cardu", email = "costantino.fe@gmail.com")))
@SpringBootApplication
public class NufemitApplication {

    public static void main(String[] args) {
        SpringApplication.run(NufemitApplication.class, args);
    }

}
