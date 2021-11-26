package com.library.demo.repositorios;

import com.library.demo.entidades.Autor;
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
public interface AutorRepositorio extends JpaRepository<Autor, String> {

    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombre")
    public Autor buscarAutorPorNombre(@Param("nombre") String nombre);

    @Query("SELECT a FROM Autor a WHERE a.alta = true")
    public List<Autor> listarAutor();

}
