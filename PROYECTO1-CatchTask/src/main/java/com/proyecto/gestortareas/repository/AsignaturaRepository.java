package com.proyecto.gestortareas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.gestortareas.entity.Asignatura;

//Permite trabajar con las asignaturas guardadas en la base de datos
public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

	// Busca las asignaturas que pertenecen a un usuario concreto
    List<Asignatura> findByUsuarioId(Long usuarioId);
}

