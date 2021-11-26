package com.library.demo.controladores;

import com.library.demo.entidades.Editorial;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.servicios.EditorialServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialServicio editorialServicio;

    @PostMapping("/registrar")
    public String registrarEditorial(ModelMap model, String nombre) {

        try {
            editorialServicio.crearEditorial(nombre);
            model.put("exito", "Registro Exitoso.");

        } catch (ErrorServicio ex) {
            Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("nombre", nombre);

            model.put("error", "Error al registrar.");

            return "editorialRegistro.html";

        }

        return "editorialRegistro.html";

    }

    @GetMapping("/consulta")
    public String listarEditoriales(ModelMap model) {

        List<Editorial> editoriales = editorialServicio.listarEditorial();

        model.put("editoriales", editoriales);

        return "editorialConsulta.html";

    }

    @GetMapping("/editar-editorial")
    public String editarEditorialVista(ModelMap model, String id) {

        Editorial editorial = new Editorial();

        if (id != null) {

            editorial = editorialServicio.buscarEditorialPorId(id);

        }

        model.put("perfil", editorial);

        return "editorialModificacion.html";

    }

    @PostMapping("/modificar-editorial")
    public String modificarEditorial(ModelMap model, String id, String nombre) {

        try {
            editorialServicio.modificarEditorial(id, nombre);
            model.put("exito", "Modificacion Exitosa");

            return "redirect:/editorial/consulta";

        } catch (ErrorServicio ex) {
            Logger.getLogger(EditorialController.class.getName()).log(Level.SEVERE, null, ex);
            model.put("error", "Error al modificar.");

            return "editorialModificacion.html";
        }

    }

    @PostMapping("/eliminar-editorial")
    public String deshabilitarEditorial(ModelMap model, String id) {

        editorialServicio.deshabilitarEditorial(id);
        model.put("exito", "Se elimino correctamente.");

        return "redirect:/editorial/consulta";

    }

}
