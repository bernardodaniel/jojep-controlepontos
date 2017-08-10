package com.db.jojeps.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "pessoas")
public class Pessoa {

	@Id
	public String id;
	
	public Integer posicao;
	public String nome;
	public String cidade;
	public Double totalPontos;
	public String sexo;
	public String endereco;
	public String email;
	public String celular;
	public String telefone;
	public String empresa;
	
	public List<Ponto> pontos = new ArrayList<>();

	@Override
	public String toString() {
		return nome + " - " + cidade + " - " + totalPontos;
	}
	
}
