package com.db.jojeps.api;

public class Ponto {
	

	public Integer seq;
	public String data;
	private Double pontuacao;
	
	@Override
	public String toString() {
		return seq + " - " + data + " - " + pontuacao;
	}
	
	public void setPontuacao(Double pontuacao) {
		this.pontuacao = pontuacao;
	}
	
	public Double getPontuacao() {
		return pontuacao != null ? pontuacao : 0;
	}

}
