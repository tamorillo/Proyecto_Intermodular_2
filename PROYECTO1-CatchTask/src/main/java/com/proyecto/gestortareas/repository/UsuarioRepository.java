package com.proyecto.gestortareas.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.gestortareas.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	// Buscará a un usuario por su email
	Usuario findByEmail(String email);

}
