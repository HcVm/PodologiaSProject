package com.PodologiaSProject.app.dao;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Cita;

@Repository
public interface CitaRepository  extends JpaRepository<Cita, Integer>{
	
	boolean existsByIdPodologoAndFechaHora(Integer idPodologo, LocalDateTime fechaHora);
}
