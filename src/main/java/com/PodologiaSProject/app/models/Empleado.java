package com.PodologiaSProject.app.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Empleado {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
	
	private String nombre;
	private String apellido;
	private String dni;
	private String telefono;
	private String email;
	
	@Column(name="fecha_ingreso")
	private String fechaIngreso;
	
	@Enumerated(EnumType.STRING)
	private TipoEmpleadoEnum cargo;

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

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public TipoEmpleadoEnum getCargo() {
		return cargo;
	}

	public void setCargo(TipoEmpleadoEnum cargo) {
		this.cargo = cargo;
	}

	public Empleado(String nombre, String apellido, String dni, String telefono, String email, String fechaIngreso,
			TipoEmpleadoEnum cargo) {
		
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
		this.telefono = telefono;
		this.email = email;
		this.fechaIngreso = fechaIngreso;
		this.cargo = cargo;
	}

	public Empleado() {
		
	}
	
	
}
