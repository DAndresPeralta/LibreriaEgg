package com.library.demo.repositorios;

import com.library.demo.entidades.Libro;
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
public interface LibroRepositorio extends JpaRepository<Libro, String> {

    @Query("SELECT l FROM Libro l WHERE l.isbn = :isbn")
    public Libro buscarLibroPorIsbn(@Param("isbn") long isbn);

    @Query("SELECT l FROM Libro l WHERE l.titulo = :titulo")
    public Libro buscarLibroPorTitulo(@Param("titulo") String titulo);

    @Query("SELECT l FROM Libro l WHERE l.alta = true")
    public List<Libro> listarLibro();

    @Query("SELECT l FROM Libro l WHERE l.id = :id")
    public List<Libro> listarLibroId(@Param("id") String id);

}
