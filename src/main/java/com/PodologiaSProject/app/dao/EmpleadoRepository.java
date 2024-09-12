package com.PodologiaSProject.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Integer>{

}
