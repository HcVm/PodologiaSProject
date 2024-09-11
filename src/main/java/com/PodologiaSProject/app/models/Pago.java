package com.PodologiaSProject.app.models;

import java.math.BigDecimal;
import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Pago {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private BigDecimal monto;
	
	@Column(name="fecha_pago")
	private Timestamp fechaPago;
	private MetodoPagoEnum metodoPago;

}
