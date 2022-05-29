package com.library.demo.servicios;

import com.library.demo.entidades.Autor;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.repositorios.AutorRepositorio;
import java.util.ArrayList;
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
public class AutorServicio {

    @Autowired
    private AutorRepositorio autorRepositorio;

    @Transactional() // sirve para evitar el try and catch
    public void crearAutor(String nombre) throws ErrorServicio {

        nombre = nombre.trim();

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        //Busca el nombre del autor en la BD.
        Autor autor = autorRepositorio.buscarAutorPorNombre(nombre);

        //Si el autor no existe, permite guardarlo como nuevo.
        if (autor == null) {

            Autor a = new Autor();
            a.setAlta(true);
            a.setNombre(nombre);

            //Persistimos el Objeto Autor en la BD.
            autorRepositorio.save(a);

        } else {
            
            //Si el autor se encuentra cargado en la BD, muestra el error.
            throw new ErrorServicio("El autor ya existe.");
            
        }

    }

    @Transactional
    public void modificarAutor(String id, String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();

            autor.setNombre(nombre);

            autorRepositorio.save(autor);
        }

    }

    @Transactional
    public void deshabilitarAutor(String id) {

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();

            autor.setAlta(false);

            autorRepositorio.save(autor);

        }

    }

    @Transactional
    public void habilitarAutor(String id) {

        Optional<Autor> respuesta = autorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Autor autor = respuesta.get();

            autor.setAlta(true);

            autorRepositorio.save(autor);

        }

    }

    public List<Autor> listarAutor() {

        return autorRepositorio.listarAutor();

    }

    public Autor buscarAutorPorId(String id) {

        return autorRepositorio.getOne(id);

    }

    public List<Autor> listarActivos() {

        //Se traen a todos los autores y se guardan en una 2da List todos los autores que estén activos
        List<Autor> todos = listarAutor();
        List<Autor> activos = new ArrayList();
        for (Autor autor : todos) {
            if (autor.getAlta()) {
                activos.add(autor);
            }
        }
        return activos;
    }

}
