package com.sandovalpurisaca.reporteservice.controller;

import com.sandovalpurisaca.reporteservice.entity.Reporte;
import com.sandovalpurisaca.reporteservice.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    // POST /reportes/  y  POST /reportes
    @PostMapping({"", "/"})
    public ResponseEntity<Reporte> registrar(@Valid @RequestBody Reporte reporte) {
        Reporte creado = reporteService.registrar(reporte);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    // GET /reportes/?offset=0&limit=5  y  GET /reportes
    @GetMapping({"", "/"})
    public List<Reporte> listar(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "5") int limit) {
        return reporteService.listar(offset, limit);
    }

    // GET /reportes/ciudadano/{dni}
    @GetMapping("/ciudadano/{dni}")
    public List<Reporte> listarPorDni(@PathVariable String dni) {
        return reporteService.listarPorDni(dni);
    }

    // GET /reportes/{id}
    @GetMapping("/{id}")
    public Reporte obtener(@PathVariable Long id) {
        return reporteService.obtener(id);
    }

    // PUT /reportes/{id}  (marca como atendido: estado 1 -> 0)
    @PutMapping("/{id}")
    public Reporte atender(@PathVariable Long id) {
        return reporteService.atender(id);
    }
}
