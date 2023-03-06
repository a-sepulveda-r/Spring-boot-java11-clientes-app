package com.bolsadeideas.springboot.backend.apirest.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService usuarioService; // Se inyecta el servicio de detalles de usuario

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(); // Se crea un objeto para encriptar contraseñas utilizando BCrypt
	}

	// A partir de Spring 2.6 se puede utilizar este método para crear el objeto
	// BCryptPasswordEncoder
	// @Bean
	// public static BCryptPasswordEncoder passwordEncoder() {
	// return new BCryptPasswordEncoder();
	// }

	@Override
	@Autowired
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.usuarioService).passwordEncoder(passwordEncoder());
		// Se configura la autenticación de usuarios utilizando el servicio de detalles
		// de usuario y el encriptador de contraseñas
	}

	@Bean("authenticationManager")
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager(); // Se configura el administrador de autenticación
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.anyRequest().authenticated() // Se requiere autenticación para todas las solicitudes
				.and()
				.csrf().disable() // Se deshabilita la protección CSRF para permitir solicitudes desde otros
									// dominios
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		// Se configura la gestión de sesiones para que sean sin estado
	}

}
