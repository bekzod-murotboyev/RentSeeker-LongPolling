package uz.pdp.rentseekerlongpolling.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.rentseekerlongpolling.entity.Owner;
import uz.pdp.rentseekerlongpolling.repository.OwnerRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final OwnerRepository ownerRepository;
    private final PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    String type;


    @Override
    public void run(String... args) {
        if (type.equalsIgnoreCase("create"))
            ownerRepository.save(new Owner(
                    "admin",
                    "admin@gmail.com",
                    passwordEncoder.encode("root123")));

    }
}
