package com.db.jojeps.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PessoaController {

	@Autowired
	private PessoaRepository pessoaRepo;
	
	@PostMapping("/admin/import")
	public String importar() {
		File file = new File("dados.csv");
		
		pessoaRepo.deleteAll();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String st;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
			Date inicio = sdf.parse("30/07/2017");
			Date fim = sdf.parse("02/10/2017");
			
			while((st = br.readLine()) != null){
				Date atual = inicio;
				String[] colunas = st.split(";");
				
				Pessoa p = new Pessoa();
				
				p.nome = colunas[0];
				p.sexo = colunas[1].substring(0, 1);
				p.endereco = colunas[2];
				p.cidade = colunas[3];
				p.email = colunas[4];
				p.celular = colunas[5];
				p.telefone = colunas[6];
				p.empresa = colunas[7];
				
				int seq = 1;
				
				do {
					Ponto pt = new Ponto();
					pt.seq = seq++;
					pt.data = sdf.format(atual);
					
					p.pontos.add(pt);
					
					Calendar cal = Calendar.getInstance();
					cal.setTime(atual);
					cal.add(Calendar.DATE, 1);
					atual = cal.getTime();
					
				} while (atual.before(fim));
			
				pessoaRepo.save(p);
				
				System.out.println(p);
			}
			
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			return "erro: " + e.getMessage();
		}
		
		return "sucesso";
	}
	
	
	@GetMapping("/admin/export")
	public List<Pessoa> export() {
		return pessoaRepo.findAll();
	}
	
	
	@GetMapping("/participantes")
	public List<Pessoa> getParticipantes() {
		return pessoaRepo.findAll();
	}
	
	@PostMapping(value = "/participantes")
	@ResponseBody
	public String save(@RequestBody List<Pessoa> pessoas) {
		pessoaRepo.save(pessoas);
		return "ok";
	}

	
}
