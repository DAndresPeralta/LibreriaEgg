package com.library.demo.servicios;

import com.library.demo.entidades.Autor;
import com.library.demo.entidades.Editorial;
import com.library.demo.entidades.Libro;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.repositorios.LibroRepositorio;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author d.andresperalta
 */
@Service
public class LibroServicio {
    
    @Autowired
    private LibroRepositorio lr;
    
    @Autowired
    private AutorServicio autorServicio;
    
    @Autowired
    private EditorialServicio editorialServicio;
    
    @Transactional() // sirve para evitar el try and catch
    public void crearLibro(long isbn, String titulo, Integer anio, Integer ejemplares, String idAutor, String idEditorial) throws ErrorServicio {
        
        validar(isbn, titulo, anio, ejemplares);
        
        try {
            
            Libro l = new Libro();
            l.setIsbn(isbn);
            l.setTitulo(titulo);
            l.setAnio(anio);
            l.setEjemplares(ejemplares);
            l.setEjemplaresPrestados(0);
            l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());
            l.setAlta(true);
            l.setAutor(autorServicio.buscarAutorPorId(idAutor));
            l.setEditorial(editorialServicio.buscarEditorialPorId(idEditorial));
            
            lr.save(l);
            
        } catch (Exception e) {
        }
        
    }
    
    @Transactional
    public void modificarLibro(String id, long isbn, String titulo, Integer anio, Integer ejemplares, String idAutor, String idEditorial) throws ErrorServicio {
        
        validar(isbn, titulo, anio, ejemplares);

//        if (ejemplaresPrestados <= 0 || ejemplaresPrestados == null || ejemplaresPrestados > ejemplares) {
//            throw new ErrorServicio("Cantidad de ejemplares totales inválida.");
//        }
        Optional<Libro> respuesta = lr.findById(id);
        
        if (respuesta.isPresent()) {
            
            Libro l = respuesta.get();
            
            l.setIsbn(isbn);
            l.setTitulo(titulo);
            l.setAnio(anio);
            l.setEjemplares(ejemplares);
//            l.setEjemplaresPrestados(ejemplaresPrestados);
            l.setEjemplaresRestantes(l.getEjemplares() - l.getEjemplaresPrestados());
            l.setAutor(autorServicio.buscarAutorPorId(idAutor));
            l.setEditorial(editorialServicio.buscarEditorialPorId(idEditorial));
            
            lr.save(l);
        }
        
    }
    
    @Transactional
    public void deshabilitarLibro(String id) {
        
        Optional<Libro> respuesta = lr.findById(id);
        
        if (respuesta.isPresent()) {
            
            Libro libro = respuesta.get();
            
            libro.setAlta(false);
            
            lr.save(libro);
            
        }
        
    }
    
    @Transactional
    public void habilitarLibro(String id) {
        
        Optional<Libro> respuesta = lr.findById(id);
        
        if (respuesta.isPresent()) {
            
            Libro libro = respuesta.get();
            
            libro.setAlta(true);
            
            lr.save(libro);
            
        }
        
    }
    
    public void validar(long isbn, String titulo, Integer anio, Integer ejemplares) throws ErrorServicio {
        
        if (isbn == 0) {
            throw new ErrorServicio("El ISBN no puede ser 0. Debe contener entre 10 y 13 dígitos");
        }
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Título vacío.");
        }
        if (anio == null || anio == 0) {
            throw new ErrorServicio("El año no puede ser nulo.");
        }
        if (ejemplares <= 0 || ejemplares == null) {
            throw new ErrorServicio("Cantidad de ejemplares totales inválida.");
        }
        
    }
    
    public List<Libro> listarLibros() {
        
        return lr.listarLibro();
        
    } //Con esto hacemos una consulta de la lista
    
    public Libro buscarLibroPorId(String id) {
        
        return lr.getOne(id);
        
    }
    
    public Libro buscarLibroPorIsbn(long isbn) {
        
        Libro libro = lr.buscarLibroPorIsbn(isbn);
        
        return libro;
        
    }
    
    
}
