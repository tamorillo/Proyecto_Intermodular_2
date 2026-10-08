package com.proyecto.gestortareas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.proyecto.gestortareas.entity.Usuario;
import com.proyecto.gestortareas.repository.UsuarioRepository;


// Clase encargada de controlar las páginas y acciones relacionadas con los usuarios.
@Controller
public class UsuarioController {
	
	// Repository que utilizamos para guardar y consultar usuarios en la base de datos.
	private final UsuarioRepository usuarioRepository;
	
	private final PasswordEncoder passwordEncoder;
	
	// Constructor
	public UsuarioController(UsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	// Cuando el usuario entra en /registro mediante el navegador,
	// se ejecutará este método.
	@GetMapping("/registro")
	//Cuando alguien visite /registro, ejecutará este método.
	public String mostrarRegistro() {
		// Muestra la página registro.html.
		return "registro"; 
	}
	
	// Se ejecuta cuando el formulario de registro se envía mediante POST.
	@PostMapping("/registro")
	public String registrarUsuario(@ModelAttribute Usuario usuario,Model model) {
		// @ModelAttribute recoge los datos del formulario y los introduce
		// automáticamente en un objeto de tipo Usuario.
		
		// Convierte la contraseña en un hash antes de guardarla
		String passwordEncriptada = passwordEncoder.encode(usuario.getPassword());
		usuario.setPassword(passwordEncriptada);
		
		
		
		usuarioRepository.save(usuario);// Guarda el usuario en la base de datos.
		
		model.addAttribute("mensaje", "Usuario registrado correctamente");
		
		// Volvemos a la página de registro después de guardar el usuario.
		return "registro";
	}
	
	// Cuando el usuario entra en /login, se ejecuta este método.
	@GetMapping("/login")
	public String mostrarLogin() {
		
		// Muestra la página login.html.
	    return "login";
	}
	
	// Se ejecuta cuando el usuario envía el formulario de inicio de sesión.
	@PostMapping("/login")
	public String iniciarSesion(@RequestParam String email,@RequestParam String password,HttpSession session,Model model) {
		
		// Buscamos en la base de datos un usuario que tenga el email introducido.
		Usuario usuario = usuarioRepository.findByEmail(email);
		
		// Comprobamos que el usuario existe y que la contraseña introducida
		// coincide con la contraseña guardada en la base de datos.
		if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
			
			// Guardamos el usuario en la sesión para recordar que ha iniciado sesión.
			session.setAttribute("usuario", usuario);
			
			// Si los datos son correctos, enviamos al usuario a la página de inicio.
			return "redirect:/inicio";
		}
		// Si el email o la contraseña son incorrectos,
		// guardamos un mensaje de error para mostrarlo en la página de login.
		model.addAttribute("error", "Email o contraseña incorrectos");
		
		// Volvemos a mostrar la página de inicio de sesión.
		return "login";
	}
	
	// Cuando el usuario entra en /inicio, se ejecuta este método.
	@GetMapping("/inicio")
	public String mostrarInicio(HttpSession session, Model model) {
		
		// Recuperamos de la sesión el usuario que inició sesión anteriormente.
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		// Si no existe ningún usuario en la sesión,
		// significa que no ha iniciado sesión.
		if (usuario == null) {
			
			// Lo enviamos a la página de login para que se identifique.
			return "redirect:/login";
		}
		
		// Pasamos el usuario a la página de inicio para poder utilizar sus datos en el HTML.
		model.addAttribute("usuario", usuario);
		// Mostramos la página inicio.html.
		return "inicio";
	}
	
	// Cuando el usuario entra en /logout, se ejecuta este método.
	@GetMapping("/logout")
	public String cerrarSesion(HttpSession session) {
		
		// Eliminamos la sesión del usuario para cerrar su sesión.
		session.invalidate();
		
		// Después de cerrar sesión, volvemos a la página de login.
		return "redirect:/login";
		
	
	}
	
	

}
