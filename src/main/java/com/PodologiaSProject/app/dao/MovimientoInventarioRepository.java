package com.PodologiaSProject.app.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.MovimientoInventario;
import com.PodologiaSProject.app.models.Producto;

@Repository
public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Integer>{
	
	List<MovimientoInventario> findByProducto(Producto producto);
}
