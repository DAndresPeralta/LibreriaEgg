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
@RequestMapping("/prestamo")
@PreAuthorize("hasAnyRole('ROLE_USUARIO_REGISTRADO')")
public class PrestamoController {

    @Autowired
    public PrestamoServicio prestamoServicio;

    @Autowired
    public LibroServicio libroServicio;

    @Autowired
    public ClienteServicio clienteServicio;

//    @GetMapping("nuevo_prestamo")
//    public String nuevoPrestamo(ModelMap model) {
//
//        try {
//
//            List<Cliente> cliente = clienteServicio.listarActivos();
//            model.put("cliente", cliente);
//
//            List<Libro> libros = libroServicio.listarLibros();
//            model.put("libros", libros);
//
//        } catch (Exception e) {
//
//            model.put("error", "Hubo un error");
//
//        }
//        
//        return "prestamo/nuevo_prestamo";
//
//    }
    @PostMapping("/registrar")
    public String crearPrestamo(ModelMap model, @RequestParam String idLibro, @RequestParam String idCliente) {

        System.out.println("ENTRO!!!!!!!!!!!!!!!!!!!!!");

        try {

            System.out.println("ENTRO TRY!!!!!!!!!");

            prestamoServicio.crearPrestamo(idLibro, idCliente);
            model.put("exito", "Prestamo otorgado.");

        } catch (Exception e) {

            model.put("error", "Error.");

        }

        return "prestamoRegistro.html";

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

    @GetMapping("/baja-cliente/{idPrestamo}")
    public String darBaja(ModelMap model, @PathVariable String idPrestamo) {

        System.out.println("ENTRO AL CONTROLLER!!!!!!!!!!!!!");
        try {

            prestamoServicio.darBaja(idPrestamo);
            model.put("exito", "Prestamo inhabilitado");

            return "redirect:/prestamo/consulta";

        } catch (Exception e) {

            model.put("error", "Error.");

            return "redirect:/prestamo/consulta";

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

            List<Prestamo> prestamos = prestamoServicio.listarPrestamosActivos();
            model.put("prestamos", prestamos);

        } catch (Exception e) {

            model.put("error", "Error.");
        }

        return "prestamoConsulta.html";

    }

}
