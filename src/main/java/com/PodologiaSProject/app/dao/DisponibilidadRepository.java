package com.PodologiaSProject.app.dao;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.DiaSemanaEnum;
import com.PodologiaSProject.app.models.Disponibilidad;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer>{
	
	boolean existsByIdPodologoAndDiaSemanaAndHoraInicioAndHoraFin(Integer idPodologo, DiaSemanaEnum diaSemana, LocalTime horaInicio, LocalTime horaFin);

    boolean existsByIdPodologoAndDiaSemanaAndHoraInicioAndHoraFinAndIdNot(Integer idPodologo, DiaSemanaEnum diaSemana, LocalTime horaInicio, LocalTime horaFin, Integer id);
    
    List<Disponibilidad> findByIdPodologo(Integer idPodologo);

    List<Disponibilidad> findByDiaSemana(DiaSemanaEnum diaSemana);

    List<Disponibilidad> findByIdPodologoAndDiaSemana(Integer idPodologo, DiaSemanaEnum diaSemana);

}
 