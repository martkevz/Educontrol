package com.educontrol.application.modelos;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_asignatura;

    @Column
    private String nombre;

    @Column
    private String codigo;

    // Relación Muchos a Uno con PeriodoAcademico
    @ManyToOne
    @JoinColumn(name = "id_periodoAcademico", referencedColumnName = "id_periodoAcademico")
    private PeriodoAcademico periodoAcademico;

    // Relación Uno a Muchos con Evaluación.
    @OneToMany(mappedBy = "asignaturaRelacion")
    private List<Evaluacion> evaluacion;

    // Relación Uno a Muchos con Horario
    @OneToMany(mappedBy = "asignatura", fetch = FetchType.LAZY)
    private List<Horario> horarios;

    // Getters y Setters
    public Integer getId_asignatura() {
        return id_asignatura;
    }

    public void setId_asignatura(Integer id_asignatura) {
        this.id_asignatura = id_asignatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public PeriodoAcademico getPeriodoAcademico() {
        return periodoAcademico;
    }

    public void setPeriodoAcademico(PeriodoAcademico periodoAcademico) {
        this.periodoAcademico = periodoAcademico;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

   public List<Evaluacion> getEvaluacion() {
    return evaluacion;
}

public void setEvaluacion(List<Evaluacion> evaluacion) {
    this.evaluacion = evaluacion;
}

}

