package com.PodologiaSProject.app.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Cita;

@Repository
public interface CitaRepository  extends JpaRepository<Cita, Integer>{
	
	boolean existsByIdPodologoAndFechaHora(Integer idPodologo, LocalDateTime fechaHora);
	
	List<Cita> findByIdPaciente(Integer idPaciente); 

    @Query("SELECT c.podologo, COUNT(c) FROM Cita c GROUP BY c.podologo")
    List<Object[]> findCitasPorPodologo();

    @Query("SELECT c.podologo, COUNT(c) FROM Cita c WHERE c.fechaHora BETWEEN :fechaInicio AND :fechaFin GROUP BY c.podologo")
    List<Object[]> findCitasPorPodologoEntreFechas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
