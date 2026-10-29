package com.sandovalpurisaca.reporteservice.service;

import com.sandovalpurisaca.reporteservice.entity.Reporte;

import java.util.List;

public interface ReporteService {

    /** POST /reportes/ - registra un reporte con estado 1. */
    Reporte registrar(Reporte reporte);

    /** GET /reportes/?offset=0&limit=5 - lista paginada. */
    List<Reporte> listar(int offset, int limit);

    /** GET /reportes/{id} - un reporte por su codigo. */
    Reporte obtener(Long id);

    /** GET /reportes/ciudadano/{dni} - reportes de un ciudadano. */
    List<Reporte> listarPorDni(String dni);

    /** PUT /reportes/{id} - cambia el estado de 1 a 0 (atendido). */
    Reporte atender(Long id);
}
