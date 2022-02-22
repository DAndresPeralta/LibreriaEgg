package com.library.demo.controladores;

import com.library.demo.entidades.Autor;
import com.library.demo.entidades.Editorial;
import com.library.demo.entidades.Libro;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.repositorios.AutorRepositorio;
import com.library.demo.repositorios.EditorialRepositorio;
import com.library.demo.servicios.AutorServicio;
import com.library.demo.servicios.EditorialServicio;
import com.library.demo.servicios.LibroServicio;
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
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author d.andresperalta
 */
@Controller
@RequestMapping("/libro")
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
public class LibroController {

    @Autowired
    private LibroServicio libroServicio;

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Autowired
    private AutorRepositorio autorRepositorio;
    
    @Autowired
    private AutorServicio autorServicio;
    
    @Autowired
    private EditorialServicio editorialServicio;

    @PostMapping("/registrar")
    public String registrarLibro(ModelMap model, @RequestParam long isbn, @RequestParam String titulo, @RequestParam Integer anio, @RequestParam Integer ejemplares, @RequestParam String idAutor, @RequestParam String idEditorial) {

        try {

            libroServicio.crearLibro(isbn, titulo, anio, ejemplares, idAutor, idEditorial);
            model.put("exito", "Registro exitoso.");

        } catch (Exception e) {
            model.put("isbn", isbn);
            model.put("titulo", titulo);
            model.put("anio", anio);
            model.put("ejemplares", ejemplares);
            model.put("autor", idAutor);
            model.put("editorial", idEditorial);

            model.put("error", "Error al registrar.");

            return "RegistroLibreria.html";
        }

        return "RegistroLibreria.html";

    }

    @GetMapping("/consulta")
    public String listarLibros(ModelMap model) {

        List<Libro> libros = libroServicio.listarLibros();

        model.put("libros", libros);

        return "Libro.html";
    }

    @GetMapping("/editar-libro")
    public String modificarLibroVista(String id, ModelMap model) {

        List<Autor> autores = autorServicio.listarActivos();
        model.put("autor", autores);

        List<Editorial> editoriales = editorialServicio.listarActivos();
        model.put("editorial", editoriales);

        Libro libro = new Libro();

        if (id != null) {

            libro = libroServicio.buscarLibroPorId(id);

        }

        model.put("perfil", libro);

        return "Editarlibro.html";

    }

//    @GetMapping("/buscar-libro-isbn")
//    public String buscarLibroVistaIsbn(ModelMap model) {
//
//        return "libroIsbn.html";
//
//    }
//
//    @PostMapping("/editar-libro-isbn")
//    public String modificarLibroVistaIsbn(long isbn, ModelMap model) {
//
//        Libro libro = new Libro();
//
//        if (isbn == 0) {
//
//            libro = libroServicio.buscarLibroPorIsbn(isbn);
//
//        }
//
//        model.put("perfil", libro);
//
//        return "Editarlibro.html";
//
//    }
    @PostMapping("/actualizar-libro")
    public String actualizarLibro(ModelMap model, String id, long isbn, String titulo, Integer anio, Integer ejemplares, String idAutor, String idEditorial) {

        try {
            libroServicio.modificarLibro(id, isbn, titulo, anio, ejemplares, idAutor, idEditorial);

            return "redirect:/libro/consulta";

        } catch (ErrorServicio ex) {
            Logger.getLogger(LibroController.class.getName()).log(Level.SEVERE, null, ex);

            return "Editarlibro.html";
        }

    }

    @PostMapping("/eliminar-libro")
    public String deshabilitarLibro(@RequestParam String id) {

        libroServicio.deshabilitarLibro(id);

        return "redirect:/libro/consulta";
    }

}
