package com.library.demo.servicios;

import com.library.demo.entidades.Cliente;
import com.library.demo.repositorios.AutorRepositorio;
import com.library.demo.repositorios.ClienteRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author d.andresperalta
 */
@Service
public class ClienteServicio {

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    public void crearCliente(String nombre, String apellido, String dni, String telefono) {

        Cliente c = new Cliente();

        c.setNombre(nombre);
        c.setApellido(apellido);
        c.setDni(dni);
        c.setTelefono(telefono);
        c.setAlta(true);

        clienteRepositorio.save(c);
    }

    public void modificarCliente(String id, String nombre, String apellido, String dni, String telefono) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Cliente c = respuesta.get();

            c.setNombre(nombre);
            c.setApellido(apellido);
            c.setDni(dni);
            c.setTelefono(telefono);
            c.setAlta(true);

            clienteRepositorio.save(c);
        }

    }

    public void deshabilitarCliente(String id) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Cliente c = respuesta.get();

            c.setAlta(false);

            clienteRepositorio.save(c);

        }

    }

    public void habilitarCliente(String id) {

        Optional<Cliente> respuesta = clienteRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Cliente c = respuesta.get();

            c.setAlta(true);

            clienteRepositorio.save(c);

        }

    }

    public Cliente buscarClientePorId(String id) {

        return clienteRepositorio.getOne(id);

    }

    public Cliente buscarClientePorDni(String dni) {

        return clienteRepositorio.buscarClientePorDni(dni);

    }

    public List<Cliente> listarTodos() {

        return clienteRepositorio.findAll();

    }

    public List<Cliente> listarActivos() {

        List<Cliente> todos = listarTodos();
        List<Cliente> activos = new ArrayList<>();

        for (Cliente cliente : todos) {
            if (cliente.getAlta()) {

                activos.add(cliente);

            }

        }
        return activos;

    }
    
    //Hacer funcion de PrestamosActivos

}
