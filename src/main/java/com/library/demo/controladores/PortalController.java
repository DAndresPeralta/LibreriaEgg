package com.library.demo.controladores;

import com.library.demo.entidades.Autor;
import com.library.demo.entidades.Editorial;
import com.library.demo.repositorios.AutorRepositorio;
import com.library.demo.repositorios.EditorialRepositorio;
import com.library.demo.servicios.AutorServicio;
import com.library.demo.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Autowired
    private AutorServicio autorServicio;

    @Autowired
    private EditorialServicio editorialServicio;

    @GetMapping("/") // Mapeo. Este método se encarga de mostrar la vista "index" retornada.
    public String principal() {

        return "principal.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/index") // Mapeo. Este método se encarga de mostrar la vista "index" retornada.
    public String index() {

        return "index.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/registro")
    public String registro(ModelMap model) {

        List<Autor> autores = autorServicio.listarActivos();
        model.put("autor", autores);

        List<Editorial> editoriales = editorialServicio.listarActivos();
        model.put("editorial", editoriales);

        return "RegistroLibreria.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/registroAutor")
    public String registroAutor() {

        return "autorRegistro.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
    @GetMapping("/registroEditorial")
    public String registroEditorial() {

        return "editorialRegistro.html";
    }

    @GetMapping("/registroUsuario")
    public String registro() {

        return "usuarioRegistro.html";
    }

    @GetMapping("/login2")
    public String login2(@RequestParam(required = false) String error, @RequestParam(required = false) String logout, ModelMap modelo) { //required indica que el parametro no es obligatorio.

        if (error != null) {
            modelo.put("error", "Credenciales incorrectas."); // Se lanza el error si se colocan mal el usuario o clave.

            return "login2.html";
        }

        if (logout != null) {
            modelo.put("logout", "Vuelva prontos.");

            return "login2.html";
        }

        return "login2.html";
    }

}
