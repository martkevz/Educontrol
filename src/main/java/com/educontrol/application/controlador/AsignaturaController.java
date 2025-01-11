package com.educontrol.application.controlador;

import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import com.educontrol.application.modelos.Asignatura;
import com.educontrol.application.modelos.AsignaturaRespository;

@Controller
public class AsignaturaController {

    @Autowired
    private AsignaturaRespository asignaturaRepository;

    // Este método es transaccional para cargar las colecciones 'evaluacion' y 'horarios' sin problemas 
    @Transactional
    public List<Asignatura> findAll() {
        List<Asignatura> asignaturas = asignaturaRepository.findAll();

        // Inicializa la colección 'evaluacion' de cada asignatura para evitar LazyInitializationException
        asignaturas.forEach(asignatura -> {
            Hibernate.initialize(asignatura.getEvaluacion());
            Hibernate.initialize(asignatura.getHorarios()); // Inicializa la colección 'horarios'
        });

        return asignaturas;
    }

    public Asignatura save(Asignatura asignatura) {
        return asignaturaRepository.save(asignatura);
    }

    public void delete(Asignatura asignatura) {
        asignaturaRepository.delete(asignatura);
    }

    public void deleteById(Integer id) {
        asignaturaRepository.deleteById(id);
    }
}
