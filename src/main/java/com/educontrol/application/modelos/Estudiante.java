package com.educontrol.application.modelos;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.Date;
import java.util.List;

@Entity
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_estudiante;

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
    private Date fechaNacimiento;

    @Column
    private String nivelAcademico;

    @Column
    private Date fechaInscripcion;

    @Column
    private String fotoUrl;

    // Relación 1 a 1 con ExpedienteAcademico
    @OneToOne(mappedBy = "estudiante")
    private ExpedienteAcademico expedienteAcademico;

    // Relacion con Asistencias
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL)
    private List<Asistencia> asistencias;

    // Getters y Setters
    public Integer getId_estudiante() {
        return id_estudiante;
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

    public void setId_estudiante(Integer id_estudiante) {
        this.id_estudiante = id_estudiante;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getNivelAcademico() {
        return nivelAcademico;
    }

    public void setNivelAcademico(String nivelAcademico) {
        this.nivelAcademico = nivelAcademico;
    }

    public Date getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(Date fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public ExpedienteAcademico getExpedienteAcademico() {
        return expedienteAcademico;
    }

    public void setExpedienteAcademico(ExpedienteAcademico expedienteAcademico) {
        this.expedienteAcademico = expedienteAcademico;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }
    
    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }
}
