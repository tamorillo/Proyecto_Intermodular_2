package com.proyecto.gestortareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gestortareas.entity.Examen;

// Con JpaRepository podemos guardar, buscar, modificar y eliminar exámenes

public interface ExamenRepository extends JpaRepository<Examen, Long> {

}
