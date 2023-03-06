package com.bolsadeideas.springboot.backend.apirest.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.bolsadeideas.springboot.backend.apirest.models.entity.Usuario;
import com.bolsadeideas.springboot.backend.apirest.models.services.IUsuarioService;

@Component
// implementa la interfaz TokenEnhancer. Esta clase se encarga de añadir
// información adicional al token que se devolverá al usuario en el proceso de
// autenticación
public class InfoAdicionalToken implements TokenEnhancer {

	// Se inyecta el servicio del Usuario
	@Autowired
	private IUsuarioService usuarioService;

	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {

		// Se obtiene el usuario de la base de datos con su username
		Usuario usuario = usuarioService.findByUsername(authentication.getName());

		// Se crea un mapa para almacenar información adicional
		Map<String, Object> info = new HashMap<>();

		// Se añade información "Hola que tal!", seguido del nombre del usuario
		info.put("info_adicional", "Hola que tal!: ".concat(authentication.getName()));

		// Se añade información sobre el usuario como nombre, apellido y correo
		info.put("nombre", usuario.getNombre());
		info.put("apellido", usuario.getApellido());
		info.put("email", usuario.getEmail());

		// Se configura la información específicada en el token
		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);

		// Se devuelve el token con los datos adicionales
		return accessToken;
	}

}
