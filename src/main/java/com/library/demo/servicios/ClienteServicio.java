package com.library.demo.servicios;

import com.library.demo.entidades.Cliente;
import com.library.demo.entidades.Prestamo;
import com.library.demo.repositorios.AutorRepositorio;
import com.library.demo.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author d.andresperalta
 */
@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public void crearCliente(String nombre, String apellido, String dni, String telefono) {

        //Creamos el Objeto Cliente.
        Cliente c = new Cliente();

        //Seteamos los atributos del Objeto Cliente.
        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setDni(dni);
        c.setTelefono(telefono);
        c.setAlta(true);

        //Persistimos el Objeto Cliente con sus atributos en la base de datos.
        clienteRepositorio.save(c);
    }

    public void modificarCliente(String id, String nombre, String apellido, String dni, String telefono) {

        //Buscamos el Objeto Cliente requerido en nuestra base de datos a travez de su ID.
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        //Si existe, modificamos sus atributos.
        if (respuesta.isPresent()) {

            Cliente c = respuesta.get();

            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setDni(dni);
            c.setTelefono(telefono);
            c.setAlta(true);

            //Persistimos el Objeto Cliente modificado en la base de datos.
            clienteRepositorio.save(c);
        }

    }

    public void deshabilitarCliente(String id) {

        //Buscamos el Objeto Cliente requerido en nuestra base de datos a travez de su ID.
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        //Si existe, seteamos su atributo Booleano a False.
        if (respuesta.isPresent()) {

            Cliente c = respuesta.get();

            c.setAlta(false);

            //Persistimos el seteo en la base de datos.
            clienteRepositorio.save(c);

        }

    }

    public void habilitarCliente(String id) {

        //Buscamos el Objeto Cliente requerido en nuestra base de datos.
        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        //Si existe, seteamos su atributo Booleano a True.
        if (respuesta.isPresent()) {

            Cliente c = respuesta.get();

            c.setAlta(true);

            //Persisitimos en la base de datos.
            clienteRepositorio.save(c);

        }

    }

    public Cliente buscarClientePorId(String id) {

        //Se busca un Objeto Cliente en particular a travez de JPARepository
        return clienteRepositorio.getOne(id);

    }

    public Cliente buscarClientePorDni(String dni) {

        return clienteRepositorio.buscarClientePorDni(dni);

    }

    public List<Cliente> listarTodos() {

        return clienteRepositorio.findAll();

    }

    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {

        //Se busca a todos los clientes y se guardan en un Array.
        List<Cliente> todos = listarTodos();
        //Creamos un Array para guardar solo los clientes activos.
        List<Cliente> activos = new ArrayList<>();

        //Recorremos el Array "todos" capturando solo los que tienen el atributo Alta = True y los guardamos en el Array "activos".
        for (Cliente cliente : todos) {
            if (cliente.getAlta()) {

                activos.add(cliente);

            }

        }
        return activos;

    }

    //Hacer funcion de PrestamosActivos
    @Transactional(readOnly = true)
    public List<Prestamo> listarPrestamosActivos(Cliente c) {

        //Citamos todos los prestamos del cliente en particular.
        List<Prestamo> todos = c.getPrestamos();

        //Guardamos en este Array solo los prestamos Activos (Alta = True).
        List<Prestamo> activos = new ArrayList<>();

        for (Prestamo prestamo : todos) {
            if (prestamo.getAlta()) {

                activos.add(prestamo);

            }

        }

        return activos;

    }
}
