package com.sandovalpurisaca.reporteservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sandovalpurisaca.reporteservice.entity.Reporte;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Prueba los 5 endpoints con una base H2 en memoria.
 * Ejecutar con:  mvn test
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Reporte nuevoReporte(String dni) {
        Reporte reporte = new Reporte();
        reporte.setDni(dni);
        reporte.setTipo("DOMICILIARIA");
        reporte.setDireccion("Av. Siempre Viva 742");
        reporte.setReferencia("Casa de prueba");
        reporte.setFecha(LocalDateTime.of(2026, 10, 29, 10, 15, 0));
        return reporte;
    }

    @Test
    void registrarReporteDevuelve201YEstadoUno() throws Exception {
        mockMvc.perform(post("/reportes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoReporte("11112222"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.dni", is("11112222")))
                .andExpect(jsonPath("$.estado", is(1)))
                .andExpect(jsonPath("$.fecha", is("2026-10-29T10:15:00")));
    }

    @Test
    void registrarConDniInvalidoDevuelve400() throws Exception {
        mockMvc.perform(post("/reportes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoReporte("123"))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errores.dni").exists());
    }

    @Test
    void listarReportesPaginados() throws Exception {
        mockMvc.perform(get("/reportes/").param("offset", "0").param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }

    @Test
    void obtenerReportePorId() throws Exception {
        String creado = mockMvc.perform(post("/reportes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoReporte("33334444"))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(creado).get("id").asLong();

        mockMvc.perform(get("/reportes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) id)));
    }

    @Test
    void obtenerReporteInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/reportes/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    void listarReportesPorDni() throws Exception {
        mockMvc.perform(post("/reportes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoReporte("55556666"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/reportes/ciudadano/{dni}", "55556666"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].dni", everyItem(is("55556666"))));
    }

    @Test
    void atenderReporteCambiaEstadoACero() throws Exception {
        String creado = mockMvc.perform(post("/reportes/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nuevoReporte("77778888"))))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        long id = objectMapper.readTree(creado).get("id").asLong();

        mockMvc.perform(put("/reportes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado", is(0)));
    }
}
