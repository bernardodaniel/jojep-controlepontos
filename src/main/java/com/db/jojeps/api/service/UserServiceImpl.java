package com.db.jojeps.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.db.jojeps.api.model.Usuario;
import com.db.jojeps.api.repository.UsuarioRepository;

@Service
public class UserServiceImpl implements UserDetailsService {

	@Autowired
	private UsuarioRepository userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = userRepo.findByUsername(username);
		
		if (usuario == null)
			throw new UsernameNotFoundException("Usuário não encontrado: " + username);
		
		if (!usuario.isAccountNonExpired())
			throw new UsernameNotFoundException("Sua conta expirou. Entre em contato com o administrador");
		
		return new User(usuario.getUsername(), usuario.getPassword(), usuario.getAuthorities());
		
	}

}
