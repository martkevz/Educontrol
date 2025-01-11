package com.educontrol.application.modelos;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_periodoAcademico;

    @Column
    private String nombrePeriodo;

    @Column
    private String fechaInicio;

    @Column
    private String fechaFin;

    // Relación Uno a Muchos con Asignatura
    @OneToMany(mappedBy = "periodoAcademico")
    private List<Asignatura> asignaturas;

    // Getters y Setters
    public Integer getId_periodoAcademico() {
        return id_periodoAcademico;
    }

    public void setId_periodoAcademico(Integer id_periodoAcademico) {
        this.id_periodoAcademico = id_periodoAcademico;
    }

    public String getNombrePeriodo() {
        return nombrePeriodo;
    }

    public void setNombrePeriodo(String nombrePeriodo) {
        this.nombrePeriodo = nombrePeriodo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public List<Asignatura> getAsignaturas() {
        return asignaturas;
    }

    public void setAsignaturas(List<Asignatura> asignaturas) {
        this.asignaturas = asignaturas;
    }
}
