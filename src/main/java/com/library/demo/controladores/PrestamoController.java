package com.library.demo.controladores;

import com.library.demo.entidades.Cliente;
import com.library.demo.entidades.Libro;
import com.library.demo.entidades.Prestamo;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.servicios.ClienteServicio;
import com.library.demo.servicios.LibroServicio;
import com.library.demo.servicios.PrestamoServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
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
@RequestMapping("/prestamo")
public class PrestamoController {

    @Autowired
    public PrestamoServicio prestamoServicio;

    @Autowired
    public LibroServicio libroServicio;

    @Autowired
    public ClienteServicio clienteServicio;

    @PostMapping("/registrar")
    public String crearPrestamo(ModelMap model, String idLibro, String idCliente) {

        try {

            prestamoServicio.crearPrestamo(idLibro, idCliente);
            model.put("exito", "Prestamo otorgado.");

        } catch (Exception e) {

            model.put("error", "Error.");

        }

        return "";

    }

    @GetMapping("/editar-prestamo")
    public String modificarPrestamoVista(ModelMap model, String idPrestamo) throws ErrorServicio {

        try {

            List<Libro> libros = libroServicio.listarLibros();
            List<Cliente> clientes = clienteServicio.listarActivos();
            Prestamo prestamo = prestamoServicio.buscarPorId(idPrestamo);

            model.put("libros", libros);
            model.put("clientes", clientes);
            model.put("prestamo", prestamo);

        } catch (Exception e) {

            model.put("error", "Error.");

        }

        return "";

    }

    @PostMapping("/modificar-prestamo")
    public String modificarPrestamo(ModelMap model, String idPrestamo, String idLibro, String idCliente) {

        try {

            prestamoServicio.modificarPrestamo(idPrestamo, idLibro, idCliente);
            model.put("exito", "Modificación exitosa.");

            return "";

        } catch (Exception e) {

            model.put("error", "Error.");

            return "";

        }

    }

    @PostMapping("/baja-cliente")
    public String darBaja(ModelMap model, String idPrestamo) {

        try {

            prestamoServicio.darBaja(idPrestamo);
            model.put("exito", "Prestamo inhabilitado");

            return "";

        } catch (Exception e) {

            model.put("error", "Error.");

            return "";

        }

    }

    @PostMapping("/alta-cliente")
    public String darAlta(ModelMap model, String idPrestamo) {

        try {

            prestamoServicio.darAlta(idPrestamo);
            model.put("exito", "Prestamo dado de alta.");

            return "";

        } catch (Exception e) {

            model.put("error", "Error.");

            return "";
        }

    }

    @GetMapping("/consulta")
    public String listarPrestamo(ModelMap model) throws ErrorServicio {

        try {

            List<Prestamo> prestamos = prestamoServicio.listarPrestamo();
            model.put("prestamos", prestamos);

        } catch (Exception e) {

            model.put("error", "Error.");
        }

        return "";

    }

}
