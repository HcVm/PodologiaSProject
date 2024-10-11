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
	
	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cita c WHERE c.podologo.id = :idPodologo AND c.fechaHora = :fechaHora")
    boolean existsByIdPodologoAndFechaHora(@Param("idPodologo") Integer idPodologo, @Param("fechaHora") LocalDateTime fechaHora);
	
	@Query("SELECT c FROM Cita c WHERE c.paciente.id = :idPaciente")
    List<Cita> findByIdPaciente(@Param("idPaciente") Integer idPaciente);
	
	@Query("SELECT c FROM Cita c WHERE c.podologo.id = :idPodologo")
    List<Cita> findByIdPodologo(@Param("idPodologo") Integer idPodologo);

    @Query("SELECT c.podologo, COUNT(c) FROM Cita c GROUP BY c.podologo")
    List<Object[]> findCitasPorPodologo();

    @Query("SELECT c.podologo, COUNT(c) FROM Cita c WHERE c.fechaHora BETWEEN :fechaInicio AND :fechaFin GROUP BY c.podologo")
    List<Object[]> findCitasPorPodologoEntreFechas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
