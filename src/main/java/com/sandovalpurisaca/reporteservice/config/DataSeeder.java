package com.sandovalpurisaca.reporteservice.config;

import com.sandovalpurisaca.reporteservice.entity.Reporte;
import com.sandovalpurisaca.reporteservice.repository.ReporteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Carga 12 reportes de ejemplo la primera vez que arranca la API
 * (solo si la tabla esta vacia). Sirve para demostrar la paginacion.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private final ReporteRepository reporteRepository;

    public DataSeeder(ReporteRepository reporteRepository) {
        this.reporteRepository = reporteRepository;
    }

    @Override
    public void run(String... args) {
        if (reporteRepository.count() > 0) {
            return;
        }

        LocalDateTime ahora = LocalDateTime.now();
        List<Reporte> ejemplos = List.of(
                crear("45871236", "DOMICILIARIA", "Av. Los Alamos 123", "Frente al parque central", 1, ahora.minusHours(2)),
                crear("45871236", "MALEZA", "Jr. Las Begonias 456", "Al costado del colegio", 0, ahora.minusDays(1)),
                crear("70215489", "DESMONTE", "Av. Universitaria 1890", "Detras del mercado", 1, ahora.minusHours(5)),
                crear("70215489", "DOMICILIARIA", "Calle Los Pinos 78", "Porton verde", 1, ahora.minusDays(2)),
                crear("32165498", "MALEZA", "Av. Grau 234", "Esquina con Jr. Puno", 0, ahora.minusDays(3)),
                crear("32165498", "DOMICILIARIA", "Jr. Amazonas 567", "Segundo piso", 1, ahora.minusHours(8)),
                crear("78945612", "DESMONTE", "Av. Mexico 1010", "Terreno vacio", 1, ahora.minusDays(4)),
                crear("78945612", "MALEZA", "Calle San Martin 345", "Frente a la iglesia", 1, ahora.minusDays(5)),
                crear("15935728", "DOMICILIARIA", "Av. Arequipa 2500", "Edificio azul", 0, ahora.minusDays(6)),
                crear("15935728", "DESMONTE", "Jr. Cusco 890", "Al final de la cuadra", 1, ahora.minusDays(7)),
                crear("96385274", "DOMICILIARIA", "Av. Brasil 780", "Junto a la bodega", 1, ahora.minusDays(8)),
                crear("96385274", "MALEZA", "Calle Lima 156", "Parque zonificado", 1, ahora.minusDays(9))
        );

        reporteRepository.saveAll(ejemplos);
    }

    private Reporte crear(String dni, String tipo, String direccion, String referencia,
                          int estado, LocalDateTime fecha) {
        Reporte reporte = new Reporte();
        reporte.setDni(dni);
        reporte.setTipo(tipo);
        reporte.setDireccion(direccion);
        reporte.setReferencia(referencia);
        reporte.setEstado(estado);
        reporte.setFecha(fecha);
        return reporte;
    }
}
