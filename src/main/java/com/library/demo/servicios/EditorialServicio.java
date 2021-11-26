package com.library.demo.servicios;

import com.library.demo.entidades.Editorial;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.repositorios.EditorialRepositorio;
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
public class EditorialServicio {

    @Autowired
    private EditorialRepositorio editorialRepositorio;

    @Transactional() // sirve para evitar el try and catch
    public void crearEditorial(String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        Editorial e = new Editorial();
        e.setAlta(true);
        e.setNombre(nombre);

        editorialRepositorio.save(e);
    }

    @Transactional
    public void modificarEditorial(String id, String nombre) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();

            editorial.setNombre(nombre);

            editorialRepositorio.save(editorial);
        }

    }

    @Transactional
    public void deshabilitarEditorial(String id) {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();

            editorial.setAlta(false);

            editorialRepositorio.save(editorial);

        }

    }

    @Transactional
    public void habilitarEditorial(String id) {

        Optional<Editorial> respuesta = editorialRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Editorial editorial = respuesta.get();

            editorial.setAlta(true);

            editorialRepositorio.save(editorial);

        }

    }

    public List<Editorial> listarEditorial() {

        return editorialRepositorio.listarEditorial();

    }

    public Editorial buscarEditorialPorId(String id) {

        return editorialRepositorio.getOne(id);

    }
    
    public List<Editorial> listarActivos() {

        //Se traen a todos los autores y se guardan en una 2da List todos los autores que estén activos
        List<Editorial> todos = listarEditorial();
        List<Editorial> activos = new ArrayList();
        for (Editorial editorial : todos) {
            if (editorial.getAlta()) {
                activos.add(editorial);
            }
        }
        return activos;
    }

}
