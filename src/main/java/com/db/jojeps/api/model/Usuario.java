package com.db.jojeps.api.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "usuarios")
public class Usuario implements UserDetails {

	@Id
	public String id;
	
	private String username;
	private String password;
	private List<Papel> papeis = new ArrayList<>();
	private List<String> cidades = new ArrayList<>();

	
	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void addPapel(Papel papel) {
		papeis.add(papel);
	}
	
	public void addCidade(String cidade) {
		this.cidades.add(cidade);
	}

	public List<String> getCidades() {
		return cidades;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return papeis;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return username + papeis;
	}
	
}
