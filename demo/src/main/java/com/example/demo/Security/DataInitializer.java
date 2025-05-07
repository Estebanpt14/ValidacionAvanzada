package com.example.demo.Security;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.Model.Usuario;
import com.example.demo.Repositories.UsuarioRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (repo.findByEmail("admin@loteria.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setEmail("admin@loteria.com");
                admin.setPassword(encoder.encode("admin123"));
                repo.save(admin);
            }
        };
    }
}

