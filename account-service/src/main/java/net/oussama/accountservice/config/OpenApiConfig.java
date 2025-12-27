package net.oussama.accountservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI accountServiceOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8081");
        server.setDescription("Account Service - Development");

        Contact contact = new Contact();
        contact.setName("Banking Team");
        contact.setEmail("support@banking.com");

        Info info = new Info()
                .title("Account Service API")
                .version("1.0.0")
                .description("Banking Account Service using CQRS and Event Sourcing with Axon Framework")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
