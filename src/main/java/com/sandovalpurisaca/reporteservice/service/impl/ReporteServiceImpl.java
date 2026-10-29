package com.sandovalpurisaca.reporteservice.service.impl;

import com.sandovalpurisaca.reporteservice.entity.Reporte;
import com.sandovalpurisaca.reporteservice.repository.ReporteRepository;
import com.sandovalpurisaca.reporteservice.service.ReporteService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServiceImpl implements ReporteService {

    private static final int LIMITE_MAXIMO = 50;

    private final ReporteRepository reporteRepository;

    public ReporteServiceImpl(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    @Transactional
    public Reporte registrar(Reporte reporte) {
        // El cliente no decide el id ni el estado: el servidor los impone.
        reporte.setId(null);
        reporte.setEstado(1);
        if (reporte.getFecha() == null) {
            reporte.setFecha(LocalDateTime.now());
        }
        return reporteRepository.save(reporte);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> listar(int offset, int limit) {
        int offsetSeguro = Math.max(offset, 0);
        int limiteSeguro = Math.min(Math.max(limit, 1), LIMITE_MAXIMO);
        return reporteRepository.findPaginated(offsetSeguro, limiteSeguro);
    }

    @Override
    @Transactional(readOnly = true)
    public Reporte obtener(Long id) {
        return reporteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No existe el reporte con id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reporte> listarPorDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "El DNI debe tener 8 digitos");
        }
        return reporteRepository.findByDniOrderByIdDesc(dni);
    }

    @Override
    @Transactional
    public Reporte atender(Long id) {
        Reporte reporte = obtener(id);
        // Idempotente: si ya estaba atendido, se devuelve sin cambios.
        reporte.setEstado(0);
        return reporteRepository.save(reporte);
    }
}
