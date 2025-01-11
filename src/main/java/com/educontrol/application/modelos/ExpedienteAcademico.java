package com.educontrol.application.modelos;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ExpedienteAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_expedienteAcademico;

    @Column
    private Float promedioGeneral;

    @Column
    private String observacionesProfesor;

    @Column
    private String accionesDisciplinarias;

    // Relación Uno a Uno con Estudiante
    @OneToOne
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante")
    private Estudiante estudiante;

    // Relación Uno a Muchos con Evaluacion
    @OneToMany(mappedBy = "expedienteAcademico")
    private List<Evaluacion> historialCalificaciones;

    // Getters y Setters
    public Integer getId_expedienteAcademico() {
        return id_expedienteAcademico;
    }

    public void setId_expedienteAcademico(Integer id_expedienteAcademico) {
        this.id_expedienteAcademico = id_expedienteAcademico;
    }

    public Float getPromedioGeneral() {
        return promedioGeneral;
    }

    public void setPromedioGeneral(Float promedioGeneral) {
        this.promedioGeneral = promedioGeneral;
    }

    public String getObservacionesProfesor() {
        return observacionesProfesor;
    }

    public void setObservacionesProfesor(String observacionesProfesor) {
        this.observacionesProfesor = observacionesProfesor;
    }

    public String getAccionesDisciplinarias() {
        return accionesDisciplinarias;
    }

    public void setAccionesDisciplinarias(String accionesDisciplinarias) {
        this.accionesDisciplinarias = accionesDisciplinarias;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public List<Evaluacion> getHistorialCalificaciones() {
        return historialCalificaciones;
    }

    public void setHistorialCalificaciones(List<Evaluacion> historialCalificaciones) {
        this.historialCalificaciones = historialCalificaciones;
    }
}

