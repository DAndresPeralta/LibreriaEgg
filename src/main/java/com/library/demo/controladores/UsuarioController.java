package com.library.demo.controladores;

import com.library.demo.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @PostMapping("/registrar")
    public String registrar(ModelMap model, String nombre, String apellido, String dni, String clave1, String clave2) {

        try {

            usuarioServicio.Registrar(nombre, apellido, dni, clave1, clave2);

            model.put("exito", "Registro Exitoso.");

        } catch (Exception e) {

            model.put("error", "Error en el Registro");

            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("dni", dni);
            model.put("clave1", clave1);
            model.put("clave2", clave2);

            return "usuarioRegistro.html";

        }

        return "usuarioRegistro.html";

    }

}
