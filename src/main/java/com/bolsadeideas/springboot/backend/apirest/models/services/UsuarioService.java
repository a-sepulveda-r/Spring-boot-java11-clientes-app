package com.bolsadeideas.springboot.backend.apirest.models.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bolsadeideas.springboot.backend.apirest.models.dao.IUsuarioDao;
import com.bolsadeideas.springboot.backend.apirest.models.entity.Usuario;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {

	// Se declara un objeto Logger para la clase UsuarioService
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	// Se declara una instancia del objeto DAO para el manejo de datos de usuario
	@Autowired
	private IUsuarioDao usuarioDao;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// Se utiliza el objeto DAO para buscar un objeto Usuario por nombre de usuario
		// en la base de datos
		Usuario usuario = usuarioDao.findByUsername(username);

		// Si no se encuentra ningún usuario con el nombre de usuario especificado, se
		// lanza una excepción UsernameNotFoundException y se registra un mensaje de
		// error en el registro
		if (usuario == null) {
			logger.error("Error en el login: no existe el usuario '" + username + "' en el sistema!");
			throw new UsernameNotFoundException(
					"Error en el login: no existe el usuario '" + username + "' en el sistema!");
		}

		// Se obtienen los roles del usuario y se crean instancias de GrantedAuthority
		// para cada uno de ellos
		List<GrantedAuthority> authorities = usuario.getRoles()
				.stream()
				.map(role -> new SimpleGrantedAuthority(role.getNombre()))
				// Se utiliza la función peek para registrar un mensaje de registro que indica
				// el rol del usuario
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());

		// Se crea un objeto User utilizando el nombre de usuario, la contraseña, la
		// habilitación y los roles obtenidos del objeto Usuario
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getEnabled(), true, true, true,
				authorities);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByUsername(String username) {
		// Este método se utiliza para buscar un objeto Usuario por nombre de usuario en
		// la base de datos.
		// Este método se utiliza para recuperar detalles de usuario en diferentes
		// partes de la aplicación
		return usuarioDao.findByUsername(username);
	}

}
