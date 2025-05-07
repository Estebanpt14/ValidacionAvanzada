package com.example.demo;

import com.example.demo.Dtos.LoginRequest;
import com.example.demo.Dtos.LoginResponse;
import com.example.demo.Model.Sorteo;
import com.example.demo.Services.SorteoService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SorteoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SorteoService sorteoService;

    @Autowired
    private ObjectMapper objectMapper;

    private Sorteo sorteoEjemplo;

    private String jwtToken;

    @BeforeEach
    public void setup() throws Exception {
        // Obtener el token
        LoginRequest loginRequest = new LoginRequest("admin@loteria.com", "admin123");

        String responseString = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponse = objectMapper.readValue(responseString, LoginResponse.class);
        jwtToken = loginResponse.getToken();

        // 🔧 Inicializar sorteoEjemplo
        sorteoEjemplo = new Sorteo();
        sorteoEjemplo.setId(1L);
        sorteoEjemplo.setNumero(1234);
        sorteoEjemplo.setMontoAGanar(500000.0);
        sorteoEjemplo.setFecha(LocalDate.of(2025, 5, 1));
        sorteoEjemplo.setNumeroGanador(4321);
    }

    private String getAuthorizationHeader() {
        return "Bearer " + jwtToken;
    }

    @Test
    public void testListarTodos() throws Exception {
        Mockito.when(sorteoService.listarTodos()).thenReturn(Arrays.asList(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos")
                        .header("Authorization", getAuthorizationHeader()))
                .andExpect(status().isOk());
    }

    @Test
    public void testListarTodosForbidden() throws Exception {
        Mockito.when(sorteoService.listarTodos()).thenReturn(Arrays.asList(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testFiltrarPorNumero() throws Exception {
        Mockito.when(sorteoService.buscarPorNumero(1234)).thenReturn(Arrays.asList(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos/filtrar?numero=1234")
                        .header("Authorization", getAuthorizationHeader()))
                .andDo(print()) // Muestra la respuesta JSON en consola
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].montoAGanar").value(500000.0));
    }

    @Test
    public void testFiltrarPorNumeroForbidden() throws Exception {
        Mockito.when(sorteoService.buscarPorNumero(1234)).thenReturn(Arrays.asList(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos/filtrar?numero=1234"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testFiltrarPorFecha() throws Exception {
        Mockito.when(sorteoService.buscarPorFecha(LocalDate.of(2025, 5, 1)))
                .thenReturn(Arrays.asList(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos/filtrar?fecha=2025-05-01")
                        .header("Authorization", getAuthorizationHeader())) // Incluir el token JWT en la cabecera
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroGanador").value(4321));
    }

    @Test
    public void testFiltrarPorFechaForbidden() throws Exception {
        Mockito.when(sorteoService.buscarPorFecha(LocalDate.of(2025, 5, 1)))
                .thenReturn(Arrays.asList(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos/filtrar?fecha=2025-05-01"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testObtenerPorId() throws Exception {
        Mockito.when(sorteoService.obtenerPorId(1L)).thenReturn(Optional.of(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos/1")
                        .header("Authorization", getAuthorizationHeader())) // Incluir el token JWT en la cabecera
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    public void testObtenerPorIdForbidden() throws Exception {
        Mockito.when(sorteoService.obtenerPorId(1L)).thenReturn(Optional.of(sorteoEjemplo));

        mockMvc.perform(get("/api/sorteos/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCrearSorteo() throws Exception {
        Mockito.when(sorteoService.crear(any(Sorteo.class))).thenReturn(sorteoEjemplo);

        mockMvc.perform(post("/api/sorteos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sorteoEjemplo))
                        .header("Authorization", getAuthorizationHeader())) // Incluir el token JWT en la cabecera
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    public void testCrearSorteoForbidden() throws Exception {
        Mockito.when(sorteoService.crear(any(Sorteo.class))).thenReturn(sorteoEjemplo);

        mockMvc.perform(post("/api/sorteos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sorteoEjemplo)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testActualizarSorteo() throws Exception {
        Mockito.when(sorteoService.actualizar(eq(1L), any(Sorteo.class))).thenReturn(sorteoEjemplo);

        mockMvc.perform(put("/api/sorteos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sorteoEjemplo))
                        .header("Authorization", getAuthorizationHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1234));
    }

    @Test
    public void testActualizarSorteoForbidden() throws Exception {
        Mockito.when(sorteoService.actualizar(eq(1L), any(Sorteo.class))).thenReturn(sorteoEjemplo);

        mockMvc.perform(put("/api/sorteos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sorteoEjemplo)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testEliminarSorteo() throws Exception {
        mockMvc.perform(delete("/api/sorteos/1")
                        .header("Authorization", getAuthorizationHeader()))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarSorteoForbidden() throws Exception {
        mockMvc.perform(delete("/api/sorteos/1"))
                .andExpect(status().isForbidden());
    }
}
