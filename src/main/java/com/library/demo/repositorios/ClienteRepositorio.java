package com.library.demo.repositorios;

import com.library.demo.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author d.andresperalta
 */

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, String>{
    
    @Query("SELECT c FROM Cliente c WHERE c.dni = :dni")
    public Cliente buscarClientePorDni(@Param("dni") String dni);
    
    @Query("SELECT c FROM Cliente c WHERE c.alta = true")
    public List<Cliente> listarCliente();
    
}
