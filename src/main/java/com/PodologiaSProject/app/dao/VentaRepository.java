package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer>{
	
	List<Venta> findByIdPaciente(Integer idPaciente);

    List<Venta> findByIdProducto(Integer idProducto);
}
