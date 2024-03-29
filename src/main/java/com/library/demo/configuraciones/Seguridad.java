package com.library.demo.configuraciones;

import com.library.demo.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author d.andresperalta
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Seguridad extends WebSecurityConfigurerAdapter{
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());

    } //Encripatcion de contraseña.

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().frameOptions().sameOrigin().and()
                .authorizeRequests()
                .antMatchers("/css/*", "/js/*", "/img/*", "/**").permitAll()
                .and().formLogin()
                .loginPage("/login2") // En que formulario esta mi login
                .loginProcessingUrl("/logincheck")
                .usernameParameter("username") // Como viajan los datos del logueo
                .passwordParameter("password")// Como viajan los datos del logueo
                .defaultSuccessUrl("/index") // A que URL viaja 
//                .failureUrl("/login2?error=error")
                .permitAll()
                .and().logout() // Aca configuro la salida
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login2?logout")
                .permitAll().and().csrf().disable();
    }

}
