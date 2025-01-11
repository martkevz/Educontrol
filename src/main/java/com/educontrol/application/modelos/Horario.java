package com.educontrol.application.modelos;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_horario;

    @Column
    private String dia;

    @Column
    private String hora;

    // Relación Muchos a Uno con Profesor
    @ManyToOne
    @JoinColumn(name = "id_profesor", referencedColumnName = "id_profesor")
    private Profesor profesor;

    // Relación Uno a Muchos con  Aula
    @OneToMany(mappedBy = "horarios")
    private List<Aula> aula;

    // Relación Muchos a Uno con Asignatura
    @ManyToOne
    @JoinColumn(name = "id_asignatura", referencedColumnName = "id_asignatura")
    private Asignatura asignatura;

    @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grupo> grupo;

    @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL)
    private List<Asistencia> asistencias;

    // Getters y Setters
    public Integer getId_horario() {
        return id_horario;
    }

    public void setId_horario(Integer id_horario) {
        this.id_horario = id_horario;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Asignatura getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(Asignatura asignatura) {
        this.asignatura = asignatura;
    }

    public List<Aula> getAulaList(){
        return this.aula;
    }

    public void setAulaList(List<Aula> a){
        this.aula = a;
    }

    public List<Grupo> getGrupoList(){
        return this.grupo;
    }

    public void setGrupoList(List<Grupo> grupos){
        this.grupo = grupos;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }
    
    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }
}
