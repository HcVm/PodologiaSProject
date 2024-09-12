package com.PodologiaSProject.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {

}
