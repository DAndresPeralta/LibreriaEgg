package com.library.demo.servicios;

import com.library.demo.entidades.Cliente;
import com.library.demo.entidades.Libro;
import com.library.demo.entidades.Prestamo;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.repositorios.ClienteRepositorio;
import com.library.demo.repositorios.LibroRepositorio;
import com.library.demo.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author d.andresperalta
 */
@Service
public class PrestamoServicio {
    
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    
    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Autowired
    private LibroServicio libroServicio;
    
    @Autowired
    private ClienteServicio clienteServicio;
    
    public void crearPrestamo(String idLibro, String idCliente) throws ErrorServicio {
        
        Libro libro = libroServicio.buscarLibroPorId(idLibro);
        
        Cliente cliente = clienteServicio.buscarClientePorId(idLibro);
        
        Prestamo prestamo = new Prestamo();
        prestamo.setFechaEntrega(new Date());
        prestamo.setFechaDevolucion(null);
        prestamo.setAlta(true);
        
        prestamo = verificarDatos(libro, cliente, prestamo);
        
        prestamoRepositorio.save(prestamo);
        
    }
    
    public void modificarPrestamo(String idPrestamo, String idLibro, String idCliente) {
        
        Libro libroNuevo = libroServicio.buscarLibroPorId(idLibro);
        
        Cliente clienteNuevo = clienteServicio.buscarClientePorId(idLibro);
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        
        if (respuesta.isPresent()) {
            
            Prestamo prestamo = respuesta.get();
            
            Libro libroViejo = prestamo.getLibro();
            
            Cliente clienteViejo = prestamo.getCliente();
            
        }
        
    }
    
    public void darBaja(String idPrestamo) throws ErrorServicio {
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        
        if (respuesta.isPresent()) {
            
            Prestamo prestamo = devolverPrestamo(respuesta.get());
            
            prestamoRepositorio.save(prestamo);
            
        } else {
            
            throw new ErrorServicio("No se ha encontrado el prestamo solicitado.");
        }
        
    }
    
    public void darAlta(String idPrestamo) throws ErrorServicio {
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        
        if (respuesta.isPresent()) {
            
            Prestamo prestamo = reactivarPrestamo(respuesta.get());
            
            prestamoRepositorio.save(prestamo);
            
        } else {
            
            throw new ErrorServicio("No se ha encontrado el prestamo solicitado.");
        }
        
    }
    
    public Prestamo buscarPorId(String idPrestamo) throws ErrorServicio {
        
        Optional<Prestamo> respuesta = prestamoRepositorio.findById(idPrestamo);
        
        if (respuesta.isPresent()) {
            
            return respuesta.get();
            
        } else {
            
            throw new ErrorServicio("No se ha encontrado el prestamo solicitado.");
        }
        
    }
    
    public List<Prestamo> listarPrestamo(String idPrestamo) throws ErrorServicio {
        
        try {
            
            return prestamoRepositorio.findAll();
        } catch (Exception e) {
            
            throw new ErrorServicio("No se han encontrado prestamos vigentes.");
        }
        
    }
    
    public Prestamo reactivarPrestamo(Prestamo prestamo) {
        
        Libro libro = prestamo.getLibro();
        Cliente cliente = prestamo.getCliente();
        
        if (libro.getEjemplaresRestantes() > 0) {
            
            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
        }
        
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() + 1);
        
        libroRepositorio.save(libro);
        clienteRepositorio.save(cliente);
        
        prestamo.setFechaDevolucion(null);
        prestamo.setAlta(true);
        
        return prestamo;
        
    }
    
    public Prestamo devolverPrestamo(Prestamo prestamo) {
        
        prestamo.setFechaDevolucion(new Date());
        prestamo.setAlta(false);
        
        Libro libro = prestamo.getLibro();
        Cliente cliente = prestamo.getCliente();
        
        libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() - 1);
        libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() + 1);
        
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() - 1);
        
        libroRepositorio.save(libro);
        clienteRepositorio.save(cliente);
        
        return prestamo;
        
    }
    
    public Prestamo verificarModificacion(Prestamo prestamo, Libro libroNuevo, Libro libroViejo, Cliente clienteNuevo, Cliente clienteViejo) throws ErrorServicio {
        
        boolean sameLibro = false;
        boolean sameCliente = false;
        
        if (libroNuevo == null) {
            
            throw new ErrorServicio("No se ha encontrado el libro solicitado.");
        }
        
        if (clienteNuevo == null) {
            
            throw new ErrorServicio("No se ha encontrado el usuario solicitado.");
        }
        
        if (!(libroNuevo.getId().equals(libroViejo.getId()))) {
            
            if (prestamo.getAlta()) {
                
                if (libroNuevo.getEjemplaresRestantes() < 1) {
                    
                    throw new ErrorServicio("No quedan ejemplares del libro.");
                }
                
                libroNuevo.setEjemplaresPrestados(libroNuevo.getEjemplaresPrestados() + 1);
                libroNuevo.setEjemplaresRestantes(libroNuevo.getEjemplaresRestantes() - 1);
                
                libroViejo.setEjemplaresPrestados(libroViejo.getEjemplaresPrestados() - 1);
                libroViejo.setEjemplaresRestantes(libroViejo.getEjemplaresRestantes() + 1);
                
                libroRepositorio.save(libroNuevo);
                libroRepositorio.save(libroViejo);
                
            }
            
            prestamo.setLibro(libroNuevo);
            
        } else {
            
            sameLibro = true;
        }
        
        if (!(clienteNuevo.getId().equals(clienteViejo.getId()))) {
            
            if (prestamo.getAlta()) {
                
                clienteNuevo.setCantidadPrestamos(clienteNuevo.getCantidadPrestamos() + 1);
                clienteViejo.setCantidadPrestamos(clienteViejo.getCantidadPrestamos() - 1);
                
                clienteRepositorio.save(clienteNuevo);
                clienteRepositorio.save(clienteViejo);
            }
            
            prestamo.setCliente(clienteNuevo);
            
        } else {
            
            sameCliente = true;
        }
        
        if (sameCliente && sameLibro) {
            
            throw new ErrorServicio("No se han registrado cambios.");
        }
        
        return prestamo;
        
    }
    
    public Prestamo verificarDatos(Libro libro, Cliente cliente, Prestamo prestamo) throws ErrorServicio {
        
        if (libro == null) { //Chequeo que la entidad libro no sea nulo.

            throw new ErrorServicio("El libro solicitaco no se encuentra en la base de datos.");
        }
        
        if (cliente == null) { //Chequeo que la entidad cliente no sea nulo.

            throw new ErrorServicio("El cliente solicitado no se encuentra en la base de datos.");
        }
        
        if (libro.getEjemplaresRestantes() > 0) { //Chequeo que el atributo ejemplares sea mayor a cero.

            libro.setEjemplaresPrestados(libro.getEjemplaresPrestados() + 1);
            libro.setEjemplaresRestantes(libro.getEjemplaresRestantes() - 1);
            
            prestamo.setLibro(libro);
            libroRepositorio.save(libro);
            
        } else {
            
            throw new ErrorServicio("El libro solicitado no se encuentra en la base de datos.");
        }
        
        cliente.setCantidadPrestamos(cliente.getCantidadPrestamos() + 1); //Si se chequea lo anterior se setea el prestamo al cliente.

        prestamo.setCliente(cliente); // Se vincula el cliente al prestamo.
        clienteRepositorio.save(cliente); // Se persiste la información en el Cliente.

        return prestamo;
        
    }
    
}
