package com.educontrol.application.modelos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpedienteAcademicoRepository extends JpaRepository<ExpedienteAcademico,Integer> {
    
}


