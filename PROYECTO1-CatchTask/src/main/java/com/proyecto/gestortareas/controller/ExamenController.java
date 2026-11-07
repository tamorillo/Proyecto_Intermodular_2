package com.proyecto.gestortareas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.proyecto.gestortareas.entity.Examen;
import com.proyecto.gestortareas.entity.Usuario; //identifica al usuario conectado
import com.proyecto.gestortareas.repository.ExamenRepository; // permite consultar y guardar los exámenes
import jakarta.servlet.http.HttpSession;// Comprueba quién ha iniciado sesión
import java.util.List; //permite trabajar con una lista de exámenes.
import com.proyecto.gestortareas.entity.Asignatura;
import com.proyecto.gestortareas.repository.AsignaturaRepository;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping("/examenes")
public class ExamenController {

	// Repositorio para acceder a los exámenes guardados.
	@Autowired
	private ExamenRepository examenRepository; 
	
	// Repositorio para acceder a las asignaturas.
	@Autowired
	private AsignaturaRepository asignaturaRepository;
	
	// Muestra los exámenes del usuario que ha iniciado sesión.
	@GetMapping
	public String listarExamenes(HttpSession session, Model model) {
		
		// Comprobamos si hay un usuario conectado.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// Si no hay ningún usuario, volvemos al inicio de sesión.
		if (usuario == null) {
			return "redirect:/login";
		}
		
		// Buscamos los exámenes asociados a ese usuario.
		List<Examen> examenes = examenRepository.findByUsuarioId(usuario.getId());
		
		// Enviamos la lista de exámenes a la página HTML.
		model.addAttribute("examenes", examenes);
		
		return "examenes";
	}
	
	// Muestra el formulario para crear un nuevo examen.
	@GetMapping("/nuevo")
	public String nuevoExamen(HttpSession session, Model model) {
		
		// Comprobamos si hay un usuario conectado.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// Si no hay ningún usuario, volvemos al inicio de sesión.
		if (usuario == null) {
			return "redirect:/login";
		}
		
		// Creamos un examen vacío para rellenar el formulario.
		model.addAttribute("examen", new Examen());
		
		// Buscamos las asignaturas del usuario conectado.
		List<Asignatura> asignaturas = asignaturaRepository.findByUsuarioId(usuario.getId());
		
		// Enviamos las asignaturas al formulario.
		model.addAttribute("asignaturas", asignaturas);
		
		return "formulario-examen";
	
	}
	
	// Guarda un examen nuevo en la base de datos.
	@PostMapping("/guardar")
	public String guardarExamen(
			
		@ModelAttribute("examen") Examen examen,
		HttpSession session,
		RedirectAttributes redirectAttributes) {
		
		// Obtiene el usuario conectado.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// Impide guardar exámenes sin iniciar sesión.
		if (usuario == null) {
			return "redirect:/login";
		}
		
		// Comprueba que se haya seleccionado una asignatura.
		if (examen.getAsignatura() == null || examen.getAsignatura().getId() == null) {
			
			redirectAttributes.addFlashAttribute(
					"error", "Debes seleccionar una asignatura.");
			
			return "redirect:/examenes/nuevo";
		}
		
		// Busca la asignatura seleccionada en la base de datos.
		Asignatura asignatura = asignaturaRepository
				.findById(examen.getAsignatura().getId())
				.orElse(null);
		
		// Comprueba que la asignatura exista y pertenezca al usuario.
		if (asignatura == null || !asignatura.getUsuario().getId().equals(usuario.getId())) {
			
			redirectAttributes.addFlashAttribute(
					"error", "La asignatura seleccionada no es válida.");
			
			return "redirect:/examenes/nuevo";
		}
		
		// Asocia el examen al usuario conectado.
		examen.setUsuario(usuario);
		
		// Asocia el examen a la asignatura seleccionada.
		examen.setAsignatura(asignatura);
		
		// Guarda el examen en la base de datos.
		examenRepository.save(examen);
		
		// Muestra un mensaje de confirmación.
		redirectAttributes.addFlashAttribute(
				"mensaje", "Examen añadido correctamente.");
		
		// Vuelve al listado de exámenes.
		return "redirect:/examenes";
				
	}
	
	// Muestra el formulario para modificar un examen.
	@GetMapping("/editar/{id}")
	public String editarExamen(
			@PathVariable Long id,
			HttpSession session,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		// Comprobamos si hay un usuario conectado.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		if (usuario == null) {
			return "redirect:/login";
		}
		
		// Buscamos el examen seleccionado.
		Examen examen = examenRepository.findById(id).orElse(null);
		
		// Comprobamos que el examen exista y pertenezca al usuario.
		if (examen == null || !examen.getUsuario().getId().equals(usuario.getId())) {
			
			redirectAttributes.addFlashAttribute(
					"error", "No se ha encontrado el examen.");
			
			return "redirect:/examenes";
			
		}
		
		// Enviamos el examen al formulario.
		model.addAttribute("examen", examen);
		
		// Buscamos las asignaturas del usuario conectado.
		List<Asignatura> asignaturas = asignaturaRepository.findByUsuarioId(usuario.getId());
		
		// Enviamos las asignaturas al formulario.
		model.addAttribute("asignaturas", asignaturas);
		
		return "formulario-examen";
	}
	
	// Actualiza los datos de un examen existente.
	@PostMapping("/actualizar/{id}")
	public String actualizarExamen(
		@PathVariable Long id,
		@ModelAttribute("examen") Examen datosExamen,
		HttpSession session,
		RedirectAttributes redirectAttributes) {
		
		// Obtiene el usuario conectado.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// Impide modificar exámenes sin iniciar sesión.
		if (usuario == null) {
			return "redirect:/login";
		}
		
		// Busca el examen que se quiere modificar.
		Examen examen = examenRepository.findById(id).orElse(null);
		
		// Comprueba que el examen exista y pertenezca al usuario.
		if (examen == null || !examen.getUsuario().getId().equals(usuario.getId())) {
			
			redirectAttributes.addFlashAttribute(
					"error", "No se ha encontrado el examen.");
			
			return "redirect:/examenes";
		}
		
		// Comprueba que se haya seleccionado una asignatura.
		if (datosExamen.getAsignatura() == null || datosExamen.getAsignatura().getId() == null) {
			
			redirectAttributes.addFlashAttribute(
					"error", "Debes seleccionar una asignatura.");
			
			return "redirect:/examenes/editar/" + id;
		}
		
		// Busca la asignatura seleccionada.
		Asignatura asignatura = asignaturaRepository
				.findById(datosExamen.getAsignatura().getId())
				.orElse(null);
		
		// Comprueba que la asignatura pertenezca al usuario.
		if (asignatura == null || !asignatura.getUsuario().getId().equals(usuario.getId())) {
			
			redirectAttributes.addFlashAttribute(
					"error", "La asignatura seleccionada no es válida.");
			
			return "redirect:/examenes/editar/" + id;
			
		}
		
		// Actualiza los datos del examen.
		examen.setNombre(datosExamen.getNombre());
		examen.setFecha(datosExamen.getFecha());
		examen.setDescripcion(datosExamen.getDescripcion());
		examen.setAsignatura(asignatura);
		
		// Guarda los cambios en la base de datos.
		examenRepository.save(examen);
		
		// Muestra un mensaje de confirmación.
		redirectAttributes.addFlashAttribute(
				"mensaje", "Examen actualizado correctamente.");
		
		// Vuelve al listado de exámenes.
		return "redirect:/examenes";
		
	}
	
	// Elimina un examen de la base de datos.
	@PostMapping("/eliminar/{id}")
	public String eliminarExamen(
		@PathVariable Long id,
		HttpSession session,
		RedirectAttributes redirectAttributes) {
		
		// Obtiene el usuario conectado.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// Impide eliminar exámenes sin iniciar sesión.
		if (usuario == null) {
			return "redirect:/login";
		}
		
		// Busca el examen que se quiere eliminar.
		Examen examen = examenRepository.findById(id).orElse(null);
		
		// Comprueba que el examen exista y pertenezca al usuario.
		if (examen == null || !examen.getUsuario().getId().equals(usuario.getId())) {
			
			redirectAttributes.addFlashAttribute(
					"error", "No se ha encontrado el examen.");
			
			return "redirect:/examenes";
		}
		
		// Elimina el examen de la base de datos.
		examenRepository.delete(examen);
		
		// Muestra un mensaje de confirmación.
		redirectAttributes.addFlashAttribute(
				"mensaje", "Examen eliminado correctamente.");
		
		// Vuelve al listado de exámenes.
		return "redirect:/examenes";
	}
		
}
