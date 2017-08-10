package com.db.jojeps.api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pontos")
public class Ponto {
	
	@Id
	public String id;
	public Integer seq;
	public String data;
	public Double pontuacao;
	
	@Override
	public String toString() {
		return seq + " - " + data + " - " + pontuacao;
	}

}
