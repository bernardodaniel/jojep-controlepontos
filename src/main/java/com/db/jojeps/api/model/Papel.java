package com.db.jojeps.api.model;

import org.springframework.security.core.GrantedAuthority;

public class Papel implements GrantedAuthority {

	private String nome;

	public Papel(String nome) {
		this.nome = nome;
	}

	@Override
	public String getAuthority() {
		return nome;
	}
	
	@Override
	public String toString() {
		return nome;
	}
}
