package com.educontrol.application.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.educontrol.application.modelos.Grupo;
import com.educontrol.application.modelos.GrupoRepository;

@Service
public class GrupoController {

    @Autowired
    private GrupoRepository grupoRepository;

    // Método para obtener todos los grupos
    public List<Grupo> findAll() {
        return grupoRepository.findAll();
    }

    // Método para buscar un grupo por ID
    public Optional<Grupo> findById(Integer id) {
        return grupoRepository.findById(id);
    }

    // Método para guardar un grupo
    public Grupo save(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    // Método para eliminar un grupo por ID
    public void deleteById(Integer id) {
        grupoRepository.deleteById(id);
    }
}
