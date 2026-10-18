package com.proyecto.gestortareas.controller;

import java.util.List;
// Indica que esta clase será un controlador web
import org.springframework.stereotype.Controller;
// Envia los datos a una pagina HTML
import org.springframework.ui.Model;
// peticiones para mostrar páginas o consultar información
import org.springframework.web.bind.annotation.GetMapping;
//peticiones que envían datos
import org.springframework.web.bind.annotation.PostMapping;
// Recoge valor direccion web, por ejemplo, el ID de una asignatura
import org.springframework.web.bind.annotation.PathVariable;
//Recoge los datos de un formulario y los relaciona con un objeto Java.
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.proyecto.gestortareas.entity.Asignatura;
// Representa al usuario que ha iniciado sesion
import com.proyecto.gestortareas.entity.Usuario;
// Para acceder a los datos de la asignatura
import com.proyecto.gestortareas.repository.AsignaturaRepository;
import jakarta.servlet.http.HttpSession;

@Controller // Esta clase atenderá peticiones web
@RequestMapping("/asignaturas")
public class AsignaturaController {

	// Permite utilizar el repositorio de asignaturas
    private final AsignaturaRepository asignaturaRepository;

    // Constructor recibe el repositorio y lo guarda en asignaturaRepository para que los métodos de la clase lo puedan utilizar
    public AsignaturaController(AsignaturaRepository asignaturaRepository) {
        this.asignaturaRepository = asignaturaRepository;
    }

    // Comprueba quien ha iniciado sesion
    private Usuario obtenerUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    } // Si no hay ningun usuario devolverá null

    // Consultar las asignaturas
    @GetMapping
    public String listarAsignaturas(HttpSession session, Model model) {
        // Obtiene al usuario que ha iniciado sesion
    	Usuario usuario = obtenerUsuario(session);

    	
        if (usuario == null) {
            return "redirect:/login";
        }

        // Obtiene una lista de asignaturas de la base de datos
        List<Asignatura> asignaturas =
                asignaturaRepository.findByUsuarioId(usuario.getId());

        // Envía la lista a la página HTML con el nombre asignaturas, para que la pueda mostrarla.
        model.addAttribute("asignaturas", asignaturas);

        return "asignaturas";
    }

    // Mostrar el formulario para añadir
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(HttpSession session, Model model) {
        if (obtenerUsuario(session) == null) {
            return "redirect:/login";
        }
        
        // Crear una asignatura
        model.addAttribute("asignatura", new Asignatura());
        model.addAttribute("accion", "nueva");

        return "formulario-asignatura";
    }

    // Guardar una asignatura nueva
    @PostMapping("/guardar")
    public String guardarAsignatura(
            @ModelAttribute("asignatura") Asignatura asignatura,
            HttpSession session,// Identifica que usuario está conectado
            RedirectAttributes redirectAttributes) { // Muestra mensaje despues de guardar

        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        asignatura.setUsuario(usuario);
        asignaturaRepository.save(asignatura);

        redirectAttributes.addFlashAttribute(
                "mensaje", "Asignatura añadida correctamente.");

        return "redirect:/asignaturas";
    }

    // Mostrar el formulario de edición
    @GetMapping("/editar/{id}") // id = representa el id de la asignatura
    public String mostrarFormularioEditar(
            @PathVariable Long id, // recoge ese número de la dirección web.
            HttpSession session,
            Model model, // envia la asignatura a la página HTML
            
            // Si no encuentra asignatura, muestra mensaje
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        Asignatura asignatura = asignaturaRepository.findById(id).orElse(null);

        if (asignatura == null
                || !asignatura.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute(
                    "error", "No se ha encontrado la asignatura.");
            return "redirect:/asignaturas";
        }

        // Si la asignatura existe y pertenece al usuario, se ejecutara esto...
        model.addAttribute("asignatura", asignatura);
        model.addAttribute("accion", "editar");

        return "formulario-asignatura";
    }

    // Guardar los cambios de una asignatura
    @PostMapping("/actualizar/{id}")
    public String actualizarAsignatura(
            @PathVariable Long id,
            @ModelAttribute("asignatura") Asignatura datosFormulario,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        Asignatura asignatura = asignaturaRepository.findById(id).orElse(null);

        if (asignatura == null
                || !asignatura.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute(
                    "error", "No se ha encontrado la asignatura.");
            return "redirect:/asignaturas";
        }

        // Si el valor introducido es correcto, actualizará los campos
        asignatura.setNombre(datosFormulario.getNombre());
        asignatura.setDescripcion(datosFormulario.getDescripcion());
        asignatura.setCurso(datosFormulario.getCurso());
        asignatura.setProfesor(datosFormulario.getProfesor());

        // Guarda los cambios
        asignaturaRepository.save(asignatura);

        redirectAttributes.addFlashAttribute(
                "mensaje", "Asignatura modificada correctamente.");

        return "redirect:/asignaturas";
    }

    // Eliminar una asignatura
    @PostMapping("/eliminar/{id}")
    public String eliminarAsignatura(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        Asignatura asignatura = asignaturaRepository.findById(id).orElse(null);

        if (asignatura == null
                || !asignatura.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute(
                    "error", "No se ha encontrado la asignatura.");
            return "redirect:/asignaturas";
        }

        asignaturaRepository.delete(asignatura);

        redirectAttributes.addFlashAttribute(
                "mensaje", "Asignatura eliminada correctamente.");

        return "redirect:/asignaturas";
    }
}