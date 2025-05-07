package com.example.demo;

import com.example.demo.Dtos.LoginRequest;
import com.example.demo.Dtos.LoginResponse;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // Esto asegura que el servidor está en un puerto aleatorio
public class AuthControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String jwtToken;

    @BeforeEach
    public void setup() {
        // Realizamos el login para obtener el JWT.
        LoginRequest loginRequest = new LoginRequest("admin@loteria.com", "admin123"); // Cambiar el correo aquí
        ResponseEntity<LoginResponse> response = restTemplate.exchange(
                "/api/auth/login",
                HttpMethod.POST,
                new HttpEntity<>(loginRequest),
                LoginResponse.class
        );

        // Asumimos que el token es retornado correctamente.
        jwtToken = response.getBody().getToken();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // Verificar que el JWT no es nulo.
        assertEquals(true, jwtToken != null && !jwtToken.isEmpty());
    }

    @Test
    public void testAccederConTokenValido() throws Exception {
        // Crear la instancia de HttpHeaders
        HttpHeaders headers = new HttpHeaders();
        // Agregar el token de autorización
        headers.set("Authorization", "Bearer " + jwtToken);

        // Crear la entidad HttpEntity con las cabeceras
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Llamada al endpoint protegido
        ResponseEntity<String> response = restTemplate.exchange("/api/sorteos", HttpMethod.GET, requestEntity, String.class);

        // Verificar el estado de la respuesta
        assertEquals(200, response.getStatusCode().value()); 
    }

    @Test
    public void testAccederSinToken() throws Exception {
        // Llamada sin token de autorización
        ResponseEntity<String> response = restTemplate.exchange("/api/sorteos", HttpMethod.GET, null, String.class);

        // Verificar el estado de la respuesta (Debería devolver 403 si no está autorizado)
        assertEquals(403, response.getStatusCode().value()); 
    }
}
