package com.PodologiaSProject.app.models;

import java.math.BigDecimal;
import java.security.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Producto {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String nombre;
	private String descripcion;
	
	@Enumerated(EnumType.STRING)
	private TipoProductoEnum tipo;
	private Integer stock;
	private BigDecimal precio;
	
	@Column(name="fecha_registro")
	private Timestamp fechaRegistro;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public TipoProductoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoProductoEnum tipo) {
		this.tipo = tipo;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public BigDecimal getPrecio() {
		return precio;
	}

	public void setPrecio(BigDecimal precio) {
		this.precio = precio;
	}

	public Timestamp getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Timestamp fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Producto(String nombre, String descripcion, TipoProductoEnum tipo, Integer stock, BigDecimal precio,
			Timestamp fechaRegistro) {
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.stock = stock;
		this.precio = precio;
		this.fechaRegistro = fechaRegistro;
	}

	public Producto() {
		
	}
	
}
