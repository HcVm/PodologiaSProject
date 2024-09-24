package com.PodologiaSProject.app.dao;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.PodologiaSProject.app.models.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer>{
	
	List<Pago> findByIdCita(Integer idCita);
	
	@Query("SELECT SUM(p.monto) FROM Pago p")
    BigDecimal calcularTotalIngresosPorCitas();

    @Query("SELECT SUM(p.monto) FROM Pago p WHERE p.fechaPago BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal calcularTotalIngresosPorCitasEntreFechas(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
