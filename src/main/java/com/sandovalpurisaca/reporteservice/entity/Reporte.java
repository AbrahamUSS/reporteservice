package com.sandovalpurisaca.reporteservice.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Reporte de acumulacion de basura.
 * estado = 1 -> pendiente (valor inicial)
 * estado = 0 -> atendido por Limpieza Publica
 */
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 digitos")
    @Column(name = "dni", nullable = false, length = 8)
    private String dni;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 30, message = "El tipo admite como maximo 30 caracteres")
    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo;

    @NotBlank(message = "La direccion es obligatoria")
    @Size(max = 200, message = "La direccion admite como maximo 200 caracteres")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    @Size(max = 255, message = "La referencia admite como maximo 255 caracteres")
    @Column(name = "referencia", length = 255)
    private String referencia;

    @Column(name = "estado", nullable = false)
    private Integer estado = 1;

    public Reporte() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}
