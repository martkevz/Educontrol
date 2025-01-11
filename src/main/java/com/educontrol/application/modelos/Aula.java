package com.educontrol.application.modelos;

import jakarta.persistence.*;

@Entity
public class Aula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_aula;

    @Column
    private String numero;

    @Column
    private int capacidad;

    @Column
    private String ubicacion;

    // Relación Muchos a uno con Horario
    @ManyToOne
    @JoinColumn(name = "id_horario", referencedColumnName = "id_horario")
    private Horario horarios;

    // Getters y Setters
    public Integer getId_aula() {
        return id_aula;
    }

    public void setId_aula(Integer id_aula) {
        this.id_aula = id_aula;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
    // comentario de prueba 

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Horario getHorarios() {
        return horarios;
    }
    //tambien por aki

    public void setHorarios(Horario horarios) {
        this.horarios = horarios;
    }
}

