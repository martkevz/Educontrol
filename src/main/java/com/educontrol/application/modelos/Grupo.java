package com.educontrol.application.modelos;

import jakarta.persistence.*;

@Entity
public class Grupo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_grupo;

    @Column(nullable = false)
    private String nombre;

    // Relación Many-to-One con Horario
    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario", nullable = false)
    private Horario horario;

    // Getters y Setters
    public Integer getId_grupo() {
        return id_grupo;
    }

    public void setId_grupo(Integer id_grupo) {
        this.id_grupo = id_grupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

}
