package com.educontrol.application.modelos;

import jakarta.persistence.*;

@Entity
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_asistencia;

    @Column 
    private boolean presente;

    // Relacion de tipo Many-to-One con la clase/entidad Estudiante
    @ManyToOne
    @JoinColumn(name = "id_estudiante", referencedColumnName = "id_estudiante", nullable = false)
    private Estudiante estudiante;

    // Relacion de tipo Many-to-One con la clase/entidad Horario
    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario", nullable = false)
    private Horario horario;

    public Integer getId_asistencia() {
        return id_asistencia;
    }
    
    public void setId_asistencia(Integer idAsistencia) {
        this.id_asistencia = idAsistencia;
    }
    
    public boolean isPresente() {
        return presente;
    }
    
    public void setPresente(boolean presente) {
        this.presente = presente;
    }
    
    public Estudiante getEstudiante() {
        return estudiante;
    }
    
    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
    
    public Horario getHorario() {
        return horario;
    }
    
    public void setHorario(Horario horario) {
        this.horario = horario;
    }
    
}
