package com.db.jojeps.api;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepo;
	
	@GetMapping(path = "/participantes")
	public List<Pessoa> getParticipantes(HttpServletResponse response) {
		
		List<Pessoa> pessoas = new ArrayList<>();
		
		Pessoa p = new Pessoa();
		
		p.posicao = 1;
		p.nome = "Peter Parker";
		p.cidade = "Maringá";
		p.totalPontos = 230.0;
		p.sexo = "M";
		
		Ponto pt = new Ponto();
		pt.seq = 1;
		pt.data = "30/07";
		pt.pontuacao = 235.0;
		
		p.pontos = new ArrayList<>();
		p.pontos.add(pt);

		pessoas.add(p);
		
		p = new Pessoa();
		
		p.posicao = 2;
		p.nome = "Tony Stark";
		p.cidade = "Cascavel";
		p.totalPontos = 220.0;
		p.sexo = "M";
		
		pt = new Ponto();
		pt.seq = 2;
		pt.data = "30/07";
		pt.pontuacao = 220.0;
		
		p.pontos = new ArrayList<>();
		p.pontos.add(pt);

		pessoas.add(p);
		
		return pessoas;
	}

	
}
