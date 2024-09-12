package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
	Paciente findByDni(String dni);
	List<Paciente> findByNombreContainingIgnoreCase(String nombre);

}
