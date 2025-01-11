package com.educontrol.application.modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;

@Entity
public class Profesor {

    public Profesor (String nombre, String apellido, String numeroIdentificacion, String direccion, String email, String especialidad){
        this.nombre = nombre;
        this.apellido = apellido;
        this.numeroIdentificacion = numeroIdentificacion;
        this.direccion = direccion;
        this.email = email;
        this.especialidad = especialidad;
    }

    public Profesor(){
        
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_profesor;

    @Column
    private String nombre;

    @Column
    private String apellido;

    @Column
    private String numeroIdentificacion;

    @Column
    private String direccion;

    @Column
    private String email;

    @Column
    private String especialidad;

    // Relación 1 a N con Horario
    @OneToMany(mappedBy = "profesor")
    private List<Horario> horarios;

    // Getters y Setters
    public Integer getId_profesor() {
        return id_profesor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumeroIdentificacion() {
        return numeroIdentificacion;
    }

    public void setNumeroIdentificacion(String numeroIdentificacion) {
        this.numeroIdentificacion = numeroIdentificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setId_profesor(Integer id_profesor) {
        this.id_profesor = id_profesor;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }
}
