package com.db.jojeps.api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pessoas")
public class Pessoa {

	@Id
	public String id;
	
	public String nome;
	
	@Override
	public String toString() {
		return nome;
	}
	
}
