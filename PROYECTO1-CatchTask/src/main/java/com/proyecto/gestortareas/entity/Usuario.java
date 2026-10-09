package com.proyecto.gestortareas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Le indicamos a JPA que esta clase representa una tabla de la base de datos.
@Entity
public class Usuario {
	
	// Indica que id es la clave primaria de la tabla.
	@Id
	
	// Le dice a MySQL que genere automáticamente el número del usuario
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//Datos que vamos a guardar de cada usuario
	private String nombre;
	private String email;
	private String password;
	
	
	// GETTERS Y SETTERS de cada atributo
	
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
	
	public String getEmail() {
        return email;
    }
	
	public void setEmail(String email) {
        this.email = email;
    }
	
	public String getPassword() {
        return password;
    }
	
	public void setPassword(String password) {
        this.password = password;
    }

}
