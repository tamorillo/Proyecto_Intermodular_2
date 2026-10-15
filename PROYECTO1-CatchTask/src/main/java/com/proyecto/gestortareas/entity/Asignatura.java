
package com.proyecto.gestortareas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

// La clase se guardará en la BD
@Entity
public class Asignatura {

    @Id // Cada asignatura tendrá un id único
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String curso;
    private String profesor;

    // permite que un usuario tenga muchas asignaturas, pero cada asignatura está asociada a un usuario
    @ManyToOne(fetch = FetchType.LAZY) // Los datos del usuario se cargan solo cuando se necesita
    
    // Creará la relación con el usuario mediante el campo usuario_id
    @JoinColumn(name = "usuario_id", nullable = false) // nullabe = false, indica que una asignatura no puede guardarse sin un usuario asociado
    private Usuario usuario;

    
    // Getters y Setters
    public Asignatura() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

