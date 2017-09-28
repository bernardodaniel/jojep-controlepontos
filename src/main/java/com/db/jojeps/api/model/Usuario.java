package com.db.jojeps.api.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
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
	private String expiraEm;

	
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
	
	public String getExpiraEm() {
		return expiraEm;
	}

	@Override
	public boolean isAccountNonExpired() {
		try {
			Date expiraDate = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(getExpiraEm());
			
			if (expiraDate.before(new Date()))
				return false;
		} catch (ParseException e) {
			return true;
		}
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
