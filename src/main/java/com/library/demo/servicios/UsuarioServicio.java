package com.library.demo.servicios;

import com.library.demo.entidades.Usuario;
import com.library.demo.excepciones.ErrorServicio;
import com.library.demo.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author d.andresperalta
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void Registrar(String nombre, String apellido, String dni, String clave, String clave2) {

        try {
            validar(nombre, apellido, dni, clave2, clave2);

            Usuario usuario = new Usuario();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setDni(dni);
            usuario.setAlta(true);

            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            usuarioRepositorio.save(usuario);

        } catch (ErrorServicio ex) {
            Logger.getLogger(UsuarioServicio.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void validar(String nombre, String apellido, String dni, String clave, String clave2) throws ErrorServicio {

        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo.");
        }

        if (apellido == null || apellido.isEmpty()) {
            throw new ErrorServicio("El apeliido no puede ser nulo.");
        }

        if (dni == null || dni.isEmpty()) {
            throw new ErrorServicio("El dni no puede ser nulo.");
        }

        if (clave == null || clave.isEmpty() || clave.length() <= 6) {
            throw new ErrorServicio("La clave no puede ser nula o contener menos de 7 dígitos.");
        }

        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves no son iguales.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException { // Este método se llama cuando algun usuario quiera autenticarse en la página.

        Usuario usuario = usuarioRepositorio.buscarPorDni(dni);

        if (usuario != null) { // Si el usuario se encuentra en la base de datos se crean los siguientes permisos y se les otorgan al usuario.

            System.out.println("SI EXISTE USUARIO!!!!!!!");
            System.out.println(usuario.getDni());
            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("ROLE_USUARIO_REGISTRADO");
            permisos.add(p1);

            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes(); //Esto nos permite reutilizar los datos del usuario logeado.
            HttpSession session = attr.getRequest().getSession(true);// los cuales se pueden utilizar en la vista, por ejemplo.
            session.setAttribute("usuariosession", usuario); // En esta linea guardamos los datos recuperados anteriormente de la BD en el objeto session.

            User user = new User(usuario.getDni(), usuario.getClave(), permisos);

            return user;

        }
        return null;

    }

}
