package com.library.demo.controladores;

import com.library.demo.entidades.Autor;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.servicios.AutorServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/autor")
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
public class AutorController {

    @Autowired
    private AutorServicio autorServicio;

    @PostMapping("/registrar")
    public String registrarAutor(ModelMap model, String nombre) {

        try {
            autorServicio.crearAutor(nombre);
            model.put("exito", "Registro Exitoso.");

        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("nombre", nombre);

            model.put("error", "Error al registrar.");

            return "autorRegistro.html";
        }

        return "autorRegistro.html";

    }

    @GetMapping("/consulta")
    public String listarAutor(ModelMap model) {

        List<Autor> autores = autorServicio.listarAutor();

        model.put("autores", autores);

        return "autorConsulta.html";

    }

    @GetMapping("/editar-autor")
    public String editarAutorVista(ModelMap model, String id) {

        Autor autor = new Autor();

        if (id != null) {
            autor = autorServicio.buscarAutorPorId(id);

        }

        model.put("perfil", autor);

        return "autorModificacion.html";

    }

    @PostMapping("/modificar-autor")
    public String modificarAutor(ModelMap model, String id, String nombre) {

        try {
            autorServicio.modificarAutor(id, nombre);
            model.put("exito", "Modificación Exitosa.");
            return "redirect:/autor/consulta";

        } catch (ErrorServicio ex) {
            Logger.getLogger(AutorController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error", "Error al modificar.");
            return "autorModificacion.html";

        }

    }

    @GetMapping("/eliminar-autor/{id}") //Paso como parametro el atributo ID para que el servicio lo busque y lo de de baja.
    public String deshabilitarAutor(ModelMap model, @PathVariable String id) {

        System.out.println("ACA BIEN");
        
        try {
            
            
            autorServicio.deshabilitarAutor(id);
            model.put("exito", "Se elimino correctamente.");

            return "redirect:/autor/consulta";

        } catch (Exception e) {

            model.put("error", "Error.");

            return "redirect:/autor/consulta";

        }

    }

}
