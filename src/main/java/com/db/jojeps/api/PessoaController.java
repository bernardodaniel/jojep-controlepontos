package com.db.jojeps.api;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepo;
	
	@PostMapping("/fill")
	public ResponseEntity<List<Pessoa>> load(HttpServletResponse response) {
		
		List<Pessoa> pessoas = new ArrayList<>();
		
		Pessoa p = new Pessoa();
		p.nome = "Teste 1";

		Pessoa p1 = new Pessoa();
		p1.nome = "Teste 2";
		
		pessoas.add(p);
		pessoas.add(p1);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().buildAndExpand("/participantes").toUri();
		
		response.setHeader("Location", uri.toASCIIString());
		
		return ResponseEntity.created(uri).body(pessoas);
	}
	
}
