package com.db.jojeps.api;

public class Ponto {
	

	public Integer seq;
	public String data;
	public Double pontuacao;
	
	@Override
	public String toString() {
		return seq + " - " + data + " - " + pontuacao;
	}

}
