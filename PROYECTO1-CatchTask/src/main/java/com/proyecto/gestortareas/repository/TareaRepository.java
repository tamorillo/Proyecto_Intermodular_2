package com.proyecto.gestortareas.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gestortareas.entity.Tarea;

//Permite acceder a las tareas guardadas en la base de datos
public interface TareaRepository extends JpaRepository<Tarea, Long> {
	
	// Busca las tareas que pertenecen a un usuario concreto
	List<Tarea> findByUsuarioId(Long usuarioId);

}
