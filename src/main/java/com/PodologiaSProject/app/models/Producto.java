package com.PodologiaSProject.app.models;

import java.math.BigDecimal;
import java.security.Timestamp;

import jakarta.persistence.Entity;
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
	private Enum<Enum<E>> tipo;
	private Integer stock;
	private BigDecimal precio;
	private Timestamp fecha_registro;
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
	public Enum<Enum<E>> getTipo() {
		return tipo;
	}
	public void setTipo(Enum<Enum<E>> tipo) {
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
	public Timestamp getFecha_registro() {
		return fecha_registro;
	}
	public void setFecha_registro(Timestamp fecha_registro) {
		this.fecha_registro = fecha_registro;
	}
	public Producto(String nombre, String descripcion, Enum<Enum<E>> tipo, Integer stock, BigDecimal precio,
			Timestamp fecha_registro) {
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.tipo = tipo;
		this.stock = stock;
		this.precio = precio;
		this.fecha_registro = fecha_registro;
	}
	public Producto() {
		
	}
	
	
}
