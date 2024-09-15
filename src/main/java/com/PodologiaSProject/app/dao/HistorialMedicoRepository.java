package com.PodologiaSProject.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.HistorialMedico;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer>{
	
	HistorialMedico findByIdAtencion(Integer idAtencion);

}
