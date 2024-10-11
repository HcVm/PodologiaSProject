package com.PodologiaSProject.app.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Integer>{
	
	@Query("SELECT v FROM Venta v WHERE v.paciente.id = :idPaciente")
    List<Venta> findByIdPaciente(@Param("idPaciente") Integer idPaciente);

	@Query("SELECT v FROM Venta v WHERE v.producto.id = :idProducto")
    List<Venta> findByIdProducto(@Param("idProducto") Integer idProducto);
    
    @Query("SELECT SUM(v.precioTotal) FROM Venta v")
    BigDecimal calcularTotalIngresosPorVentas();

    @Query("SELECT SUM(v.precioTotal) FROM Venta v WHERE v.fechaVenta BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calcularTotalIngresosPorVentasEntreFechas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
