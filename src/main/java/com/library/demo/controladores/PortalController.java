package com.library.demo.controladores;

import com.library.demo.entidades.Autor;
import com.library.demo.entidades.Editorial;
import com.library.demo.repositorios.AutorRepositorio;
import com.library.demo.repositorios.EditorialRepositorio;
import com.library.demo.servicios.AutorServicio;
import com.library.demo.servicios.EditorialServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String index() {

        return "index.html";

    }

    @GetMapping("/registro")
    public String registro(ModelMap model) {

        List<Autor> autores = autorServicio.listarActivos();
        model.put("autor", autores);

        List<Editorial> editoriales = editorialServicio.listarActivos();
        model.put("editorial", editoriales);

        return "RegistroLibreria.html";

    }

    @GetMapping("/registroAutor")
    public String registroAutor() {

        return "autorRegistro.html";
    }

    @GetMapping("/registroEditorial")
    public String registroEditorial() {

        return "editorialRegistro.html";
    }

}
