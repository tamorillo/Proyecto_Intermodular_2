package com.proyecto.gestortareas.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.proyecto.gestortareas.entity.Tarea;
import com.proyecto.gestortareas.entity.Usuario;
import com.proyecto.gestortareas.entity.Asignatura;
import com.proyecto.gestortareas.repository.TareaRepository;
import com.proyecto.gestortareas.repository.AsignaturaRepository;

import jakarta.servlet.http.HttpSession;

@Controller 
@RequestMapping("/tareas") 
public class TareaController {

    // Permite acceder a las tareas guardadas en la base de datos.
    private final TareaRepository tareaRepository;

    // Permite consultar las asignaturas disponibles.
    private final AsignaturaRepository asignaturaRepository;

    // Recibe los repositorios necesarios para gestionar las tareas.
    public TareaController(TareaRepository tareaRepository,
                           AsignaturaRepository asignaturaRepository) {
        this.tareaRepository = tareaRepository;
        this.asignaturaRepository = asignaturaRepository;
    }

    // Obtiene el usuario que ha iniciado sesión.
    private Usuario obtenerUsuario(HttpSession session) {
        return (Usuario) session.getAttribute("usuario");
    }

    // Muestra las tareas del usuario conectado.
    @GetMapping
    public String listarTareas(HttpSession session, Model model) {

        // Comprueba si hay un usuario conectado.
        Usuario usuario = obtenerUsuario(session);

        // Si no hay sesión, vuelve a la página de inicio de sesión.
        if (usuario == null) {
            return "redirect:/login";
        }

        // Busca las tareas que pertenecen al usuario conectado.
        List<Tarea> tareas =
                tareaRepository.findByUsuarioId(usuario.getId());

        // Envía las tareas a la página HTML.
        model.addAttribute("tareas", tareas);

        // Devuelve la página que mostrará el listado de tareas.
        return "tareas";
    }

    // Muestra el formulario para crear una tarea nueva.
    @GetMapping("/nueva")
    public String mostrarFormularioNueva(HttpSession session, Model model) {

        // Comprueba que el usuario haya iniciado sesión.
        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Crea un objeto vacío para rellenarlo desde el formulario.
        model.addAttribute("tarea", new Tarea());

        // Obtiene las asignaturas del usuario para elegir una en el formulario.
        List<Asignatura> asignaturas =
                asignaturaRepository.findByUsuarioId(usuario.getId());

        model.addAttribute("asignaturas", asignaturas);

        // Indica que el formulario se utiliza para crear una tarea.
        model.addAttribute("accion", "nueva");

        // Abre la página del formulario.
        return "formulario-tarea";
    }

    // Guarda una tarea nueva en la base de datos.
    @PostMapping("/guardar")
    public String guardarTarea(
            @ModelAttribute("tarea") Tarea tarea,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Obtiene el usuario conectado.
        Usuario usuario = obtenerUsuario(session);

        // Impide guardar tareas sin iniciar sesión.
        if (usuario == null) {
            return "redirect:/login";
        }

        // Comprueba que se haya seleccionado una asignatura.
        if (tarea.getAsignatura() == null
                || tarea.getAsignatura().getId() == null) {

            redirectAttributes.addFlashAttribute(
                    "error", "Debes seleccionar una asignatura.");

            return "redirect:/tareas/nueva";
        }

        // Busca la asignatura seleccionada en la base de datos.
        Asignatura asignatura = asignaturaRepository
                .findById(tarea.getAsignatura().getId())
                .orElse(null);

        // Comprueba que la asignatura exista y pertenezca al usuario.
        if (asignatura == null
                || !asignatura.getUsuario().getId().equals(usuario.getId())) {

            redirectAttributes.addFlashAttribute(
                    "error", "La asignatura seleccionada no es válida.");

            return "redirect:/tareas/nueva";
        }

        // Asocia la tarea al usuario conectado.
        tarea.setUsuario(usuario);

        // Asocia la tarea a la asignatura seleccionada.
        tarea.setAsignatura(asignatura);

        // Las tareas nuevas empiezan sin estar terminadas.
        tarea.setTerminada(false);

        // Guarda la tarea en la base de datos.
        tareaRepository.save(tarea);

        // Muestra un mensaje de confirmación.
        redirectAttributes.addFlashAttribute(
                "mensaje", "Tarea añadida correctamente.");

        // Vuelve al listado de tareas.
        return "redirect:/tareas";
    }
    

    // Muestra el formulario con los datos de la tarea que queremos editar.
    @GetMapping("/editar/{id}")
    public String editarTarea(@PathVariable Long id, Model model, HttpSession session) {

        // Comprobamos que haya un usuario conectado.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscamos la tarea por su identificador.
        Tarea tarea = tareaRepository.findById(id).orElse(null);
        //System.out.println("Fecha de la tarea: " + tarea.getFechaEntrega());

        // Comprobamos que la tarea exista y pertenezca al usuario conectado.
        if (tarea == null || !tarea.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/tareas";
        }

        // Cargamos las asignaturas del usuario para mostrarlas en el formulario.
        model.addAttribute("tarea", tarea);
        model.addAttribute("asignaturas",
                asignaturaRepository.findByUsuarioId(usuario.getId()));

        // Indicamos que el formulario se utilizará para editar.
        model.addAttribute("accion", "editar");

        return "formulario-tarea";
    }

    // Guarda los cambios realizados en una tarea existente.
    @PostMapping("/actualizar/{id}")
    public String actualizarTarea(@PathVariable Long id,
                                  @ModelAttribute("tarea") Tarea datosTarea,
                                  Model model,
                                  HttpSession session,
                                  RedirectAttributes redirectAttributes) {

        // Comprobamos que haya un usuario conectado.
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscamos la tarea original para conservar su propietario.
        Tarea tarea = tareaRepository.findById(id).orElse(null);

        // Impedimos modificar tareas que no pertenezcan al usuario.
        if (tarea == null || !tarea.getUsuario().getId().equals(usuario.getId())) {
            return "redirect:/tareas";
        }

        // Comprobamos que se haya seleccionado una asignatura válida.
        if (datosTarea.getAsignatura() == null
                || datosTarea.getAsignatura().getId() == null) {

            model.addAttribute("error", "Debes seleccionar una asignatura.");
            model.addAttribute("tarea", tarea);
            model.addAttribute("asignaturas",
                    asignaturaRepository.findByUsuarioId(usuario.getId()));
            model.addAttribute("accion", "editar");

            return "formulario-tarea";
        }

        // Comprobamos que la asignatura exista y pertenezca al usuario.
        Asignatura asignatura = asignaturaRepository
                .findById(datosTarea.getAsignatura().getId()).orElse(null);

        if (asignatura == null
                || !asignatura.getUsuario().getId().equals(usuario.getId())) {

            model.addAttribute("error", "La asignatura seleccionada no es válida.");
            model.addAttribute("tarea", tarea);
            model.addAttribute("asignaturas",
                    asignaturaRepository.findByUsuarioId(usuario.getId()));
            model.addAttribute("accion", "editar");

            return "formulario-tarea";
        }

        // Actualizamos solamente los campos que se pueden modificar.
        tarea.setNombre(datosTarea.getNombre());
        tarea.setDescripcion(datosTarea.getDescripcion());
        if (datosTarea.getFechaEntrega() != null) {
            tarea.setFechaEntrega(datosTarea.getFechaEntrega());
        }
        tarea.setAsignatura(asignatura);

        // Guardamos la tarea actualizada en la base de datos.
        tareaRepository.save(tarea);

        // Mostramos un mensaje de confirmación al volver al listado.
        redirectAttributes.addFlashAttribute("mensaje", "Tarea actualizada correctamente.");

        return "redirect:/tareas";
    }
    
    // Elimina una tarea del usuario que ha iniciado sesión.
    @PostMapping("/eliminar/{id}")
    public String eliminarTarea(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {

        // Comprobamos que el usuario haya iniciado sesión.
        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscamos la tarea que queremos eliminar.
        Tarea tarea = tareaRepository.findById(id).orElse(null);

        // Comprobamos que la tarea exista y pertenezca al usuario actual.
        if (tarea == null || !tarea.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado la tarea.");
            return "redirect:/tareas";
        }

        // Eliminamos la tarea de la base de datos.
        tareaRepository.delete(tarea);

        // Mostramos un mensaje de confirmación.
        redirectAttributes.addFlashAttribute("mensaje", "Tarea eliminada correctamente.");

        // Volvemos al listado de tareas.
        return "redirect:/tareas";
    }
    

    // Cambia el estado de una tarea entre pendiente y terminada.
    @PostMapping("/estado/{id}")
    public String cambiarEstadoTarea(@PathVariable Long id,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {

        // Comprobamos que el usuario haya iniciado sesión.
        Usuario usuario = obtenerUsuario(session);

        if (usuario == null) {
            return "redirect:/login";
        }

        // Buscamos la tarea que queremos modificar.
        Tarea tarea = tareaRepository.findById(id).orElse(null);

        // Comprobamos que la tarea exista y pertenezca al usuario actual.
        if (tarea == null || !tarea.getUsuario().getId().equals(usuario.getId())) {
            redirectAttributes.addFlashAttribute("error", "No se ha encontrado la tarea.");
            return "redirect:/tareas";
        }

        // Cambiamos el estado actual de la tarea.
        tarea.setTerminada(!tarea.isTerminada());

        // Guardamos el nuevo estado en la base de datos.
        tareaRepository.save(tarea);

        // Volvemos al listado de tareas.
        return "redirect:/tareas";
    }
}