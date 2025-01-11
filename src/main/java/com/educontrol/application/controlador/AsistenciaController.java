package com.educontrol.application.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educontrol.application.modelos.Asistencia;
import com.educontrol.application.modelos.AsistenciaRepository;

@Service
public class AsistenciaController {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    // Método para obtener todas las asistencias
    public List<Asistencia> findAll() {
        return asistenciaRepository.findAll();
    }

    // Método para buscar una asistencia por ID
    public Optional<Asistencia> findById(Integer id) {
        return asistenciaRepository.findById(id);
    }

    // Método para guardar una asistencia
    public Asistencia save(Asistencia asistencia) {
        return asistenciaRepository.save(asistencia);
    }

    // Método para eliminar una asistencia por ID
    public void deleteById(Integer id) {
        asistenciaRepository.deleteById(id);
    }

    // Método para eliminar una asistencia
    public void delete(Asistencia entity) {
        asistenciaRepository.delete(entity);
    }
}
