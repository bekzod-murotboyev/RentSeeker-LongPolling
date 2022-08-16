package uz.pdp.rentseekerlongpolling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import uz.pdp.rentseekerlongpolling.config.openApiConfig.OpenApiProperties;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition
@EnableConfigurationProperties({
        OpenApiProperties.class
})
public class RentSeekerLongPollingApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentSeekerLongPollingApplication.class, args);
    }

}
