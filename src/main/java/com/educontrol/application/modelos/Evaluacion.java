package com.educontrol.application.modelos;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_evaluacion;

    @Column
    private Date fechaEvaluacion;

    @Column
    private String tipo;

    @Column
    private Float calificacion;

    @Column
    private String comentariosProfesor;

    // Relación Muchos a Uno con ExpedienteAcademico
    @ManyToOne
    @JoinColumn(name = "id_expedienteAcademico", referencedColumnName = "id_expedienteAcademico")
    private ExpedienteAcademico expedienteAcademico;

    // Relación Muchos a Uno con Asignatura
    @ManyToOne
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id_asignatura")
    private Asignatura asignaturaRelacion;

    // Getters y Setters
    public Integer getId_evaluacion() {
        return id_evaluacion;
    }

    public void setId_evaluacion(Integer id_evaluacion) {
        this.id_evaluacion = id_evaluacion;
    }

    public Date getFechaEvaluacion() {
        return fechaEvaluacion;
    }

    public void setFechaEvaluacion(Date fechaEvaluacion) {
        this.fechaEvaluacion = fechaEvaluacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Float calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentariosProfesor() {
        return comentariosProfesor;
    }

    public void setComentariosProfesor(String comentariosProfesor) {
        this.comentariosProfesor = comentariosProfesor;
    }

    public ExpedienteAcademico getExpedienteAcademico() {
        return expedienteAcademico;
    }

    public void setExpedienteAcademico(ExpedienteAcademico expedienteAcademico) {
        this.expedienteAcademico = expedienteAcademico;
    }

    public Asignatura getAsignaturaRelacion() {
        return asignaturaRelacion;
    }

    public void setAsignaturaRelacion(Asignatura asignaturaRelacion) {
        this.asignaturaRelacion = asignaturaRelacion;
    }
}

