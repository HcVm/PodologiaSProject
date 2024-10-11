package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.HistorialMedico;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer>{
	
	@Query("SELECT hm FROM HistorialMedico hm WHERE hm.atencion.id = :idAtencion")
    HistorialMedico findByIdAtencion(@Param("idAtencion") Integer idAtencion);
	
	@Query("SELECT hm FROM HistorialMedico hm WHERE hm.paciente.id = :idPaciente")
    List<HistorialMedico> findByIdPaciente(@Param("idPaciente") Integer idPaciente);

}
