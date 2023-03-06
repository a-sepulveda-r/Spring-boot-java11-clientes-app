package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.Arrays;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableResourceServer
// Esta clase configura el servidor de recursos y se encarga del acceso a los
// recursos de nuestra aplicación.
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		// Se configuran las reglas de seguridad para las solicitudes HTTP que entran a
		// la aplicación
		http.authorizeRequests()
				// Los siguientes endpoints son públicos y están disponibles para cualquier
				// usuario
				.antMatchers(HttpMethod.GET, "/api/clientes", "/api/clientes/page/**", "/api/uploads/img/**",
						"/images/**")
				.permitAll()
				// // El siguiente endpoint solo está disponible para usuarios con los roles
				// "USER"
				// // o "ADMIN"
				// .antMatchers(HttpMethod.GET, "/api/clientes/{id}").hasAnyRole("USER",
				// "ADMIN")
				// // El siguiente endpoint solo está disponible para usuarios con los roles
				// "USER"
				// // o "ADMIN"
				// .antMatchers(HttpMethod.POST, "/api/clientes/upload").hasAnyRole("USER",
				// "ADMIN")
				// // El siguiente endpoint solo está disponible para usuarios con el rol
				// "ADMIN"
				// .antMatchers(HttpMethod.POST, "/api/clientes").hasRole("ADMIN")
				// // Los siguientes endpoints solo están disponibles para usuarios con el rol
				// // "ADMIN"
				// .antMatchers("/api/clientes/**").hasRole("ADMIN")
				// // Todas las demás solicitudes deben estar autenticadas (es decir, el usuario
				// // debe iniciar sesión)
				.anyRequest().authenticated()
				.and().cors().configurationSource(corsConfigurationSource());
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);
		config.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(
				new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return bean;
	}
}
