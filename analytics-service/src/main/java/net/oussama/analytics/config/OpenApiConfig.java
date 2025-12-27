package net.oussama.analytics.config;

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
    public OpenAPI analyticsServiceOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8082");
        server.setDescription("Analytics Service - Development");

        Contact contact = new Contact();
        contact.setName("Banking Team");
        contact.setEmail("support@banking.com");

        Info info = new Info()
                .title("Analytics Service API")
                .version("1.0.0")
                .description("Banking Analytics Service - Real-time event processing with Kafka")
                .contact(contact);

        return new OpenAPI()
                .info(info)
                .servers(List.of(server));
    }
}
