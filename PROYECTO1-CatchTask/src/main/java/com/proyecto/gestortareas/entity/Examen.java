package com.proyecto.gestortareas.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Examen {
	
	//Identifican el examen y generan su ID automáticamente
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //identifica cada examen y se asigna automáticamente.
	private String nombre; // guarda el nombre del examen
	private LocalDate fecha; // almacena el día en que se realizará el examen
	private String descripcion;
	
	// Relación con la asignatura y el usuario
	
	@ManyToOne(fetch = FetchType.LAZY)//Carga los datos relacionados cuando sean necesarios
	@JoinColumn(name = "usuario_id", nullable = false)
	private Usuario usuario;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asignatura_id", nullable = false)
	private Asignatura asignatura;
	
	// Constructor Getters y Setters
	public Examen() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Asignatura getAsignatura() {
		return asignatura;
	}

	public void setAsignatura(Asignatura asignatura) {
		this.asignatura = asignatura;
	}

}
