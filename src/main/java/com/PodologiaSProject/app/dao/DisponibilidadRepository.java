package com.PodologiaSProject.app.dao;

import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.DiaSemanaEnum;
import com.PodologiaSProject.app.models.Disponibilidad;

@Repository
public interface DisponibilidadRepository extends JpaRepository<Disponibilidad, Integer>{

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Disponibilidad d WHERE d.podologo.id = :idPodologo AND d.diaSemana = :diaSemana AND d.horaInicio = :horaInicio AND d.horaFin = :horaFin")
    boolean existsByIdPodologoAndDiaSemanaAndHoraInicioAndHoraFin(@Param("idPodologo") Integer idPodologo, @Param("diaSemana") DiaSemanaEnum diaSemana, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin);

    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Disponibilidad d WHERE d.podologo.id = :idPodologo AND d.diaSemana = :diaSemana AND d.horaInicio = :horaInicio AND d.horaFin = :horaFin AND d.id <> :id")
    boolean existsByIdPodologoAndDiaSemanaAndHoraInicioAndHoraFinAndIdNot(@Param("idPodologo") Integer idPodologo, @Param("diaSemana") DiaSemanaEnum diaSemana, @Param("horaInicio") LocalTime horaInicio, @Param("horaFin") LocalTime horaFin, @Param("id") Integer id);
    
    @Query("SELECT d FROM Disponibilidad d WHERE d.podologo.id = :idPodologo")
    List<Disponibilidad> findByIdPodologo(@Param("idPodologo") Integer idPodologo);

    List<Disponibilidad> findByDiaSemana(DiaSemanaEnum diaSemana);

    @Query("SELECT d FROM Disponibilidad d WHERE d.podologo.id = :idPodologo AND d.diaSemana = :diaSemana")
    List<Disponibilidad> findByIdPodologoAndDiaSemana(@Param("idPodologo") Integer idPodologo, @Param("diaSemana") DiaSemanaEnum diaSemana);

}