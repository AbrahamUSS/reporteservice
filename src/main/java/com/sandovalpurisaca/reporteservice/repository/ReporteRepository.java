package com.sandovalpurisaca.reporteservice.repository;

import com.sandovalpurisaca.reporteservice.entity.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Reporte, Long> {

    /**
     * Paginacion real por offset/limit, tal como la pide el examen:
     * GET /reportes/?offset=0&limit=5
     */
    @Query(value = "SELECT * FROM reportes ORDER BY id DESC LIMIT :limit OFFSET :offset",
            nativeQuery = true)
    List<Reporte> findPaginated(@Param("offset") int offset, @Param("limit") int limit);

    /** Todos los reportes de un ciudadano (los mas recientes primero). */
    List<Reporte> findByDniOrderByIdDesc(String dni);
}
