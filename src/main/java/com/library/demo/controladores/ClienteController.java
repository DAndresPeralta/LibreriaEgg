package com.library.demo.controladores;

import com.library.demo.entidades.Cliente;
import com.library.demo.servicios.ClienteServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    public ClienteServicio clienteServicio;

    @PostMapping("/registrar")
    public String crearCliente(ModelMap model, @RequestParam String nombre, @RequestParam String apellido, @RequestParam String dni, @RequestParam String telefono) {

        try {

            clienteServicio.crearCliente(nombre, apellido, dni, telefono);
            model.put("exito", "Registro Exitoso.");

        } catch (Exception e) {
            model.put("error", "Error en el registro");
            model.put("nombre", nombre);
            model.put("apellido", apellido);
            model.put("dni", dni);
            model.put("telefono", telefono);
        }

        return "clienteRegistro.html";

    }

    @GetMapping("/editar-cliente/{id}")
    public String modificarClienteVista(ModelMap model, @PathVariable String id) {

        Cliente cliente = new Cliente();

        if (id != null) {

            cliente = clienteServicio.buscarClientePorId(id);

        }

        model.put("perfil", cliente);

        return "clienteModificacion.html";
    }

    @PostMapping("/modificar-cliente")
    public String modificarAutor(ModelMap model,String id, String nombre, String apellido, String dni, String telefono) {

        try {

            clienteServicio.modificarCliente(id, nombre, apellido, dni, telefono);
            model.put("exito", "Cliente modificado.");

            return "redirect:/cliente/consulta";

        } catch (Exception e) {

            model.put("error", "Error en la modificación.");
            model.put("nombre", nombre); //POSIBLE ERROR.
            model.put("apellido", apellido);
            model.put("dni", dni);
            model.put("telefono", telefono);

            return "clienteModificacion.html";
        }

    }

    @GetMapping("/baja-cliente/{id}") //Paso como parametro el atributo ID para que el servicio lo busque y lo de de baja.
    public String darBaja(ModelMap model, @PathVariable String id) {

        try {

            clienteServicio.deshabilitarCliente(id);
            model.put("exito", "Cliente inhabilitado.");

            return "redirect:/cliente/consulta";
            
        } catch (Exception e) {

            model.put("error", "Error.");

            return "redirect:/cliente/consulta";
        }

    }

    @PostMapping("/alta-cliente")
    public String darAlta(ModelMap model, String id) {

        try {

            clienteServicio.habilitarCliente(id);
            model.put("exito", "Cliente dado de alta.");

            return "";

        } catch (Exception e) {

            model.put("error", "Error.");

            return "";
        }

    }

    @GetMapping("/consulta")
    public String consulta(ModelMap model) {

        List<Cliente> clientes = clienteServicio.listarActivos();
        model.put("clientes", clientes);

        return "clienteConsulta.html";

    }

}
