package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Tratamiento;

@Repository
public interface TratamientoRepository extends JpaRepository<Tratamiento, Integer>{
	
	List<Tratamiento> findByNombreContainingIgnoreCase(String nombre); 
}
