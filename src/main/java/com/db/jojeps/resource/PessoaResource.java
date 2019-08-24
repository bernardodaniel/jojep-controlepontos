package com.db.jojeps.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.jojeps.api.model.Pessoa;
import com.db.jojeps.api.model.Ponto;
import com.db.jojeps.api.model.Usuario;
import com.db.jojeps.api.repository.PessoaRepository;
import com.db.jojeps.api.repository.UsuarioRepository;

@RestController
public class PessoaResource {

	@Autowired
	private PessoaRepository pessoaRepo;
	
	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@GetMapping("/admin/import")
	public String importar() {
		File file = new File("dados_2019.csv");
		
		pessoaRepo.deleteAll();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String st;
			
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
			Date inicio = sdf.parse("11/08/2019");
			Date fim = sdf.parse("12/10/2019");
			
			while((st = br.readLine()) != null){
				Date atual = inicio;
				String[] colunas = st.split(";");
				
				Pessoa p = new Pessoa();
				
				p.nome = colunas[0].trim();
				p.sexo = colunas[1];
				p.endereco = colunas[2].trim();
				p.cidade = colunas[3].trim();
				p.email = colunas[4].trim();
				p.celular = colunas[5].trim();
//				p.telefone = colunas[6].trim();
//				p.empresa = colunas[7].trim();
				
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
		Order orderTotalPontos = new Order(Sort.Direction.DESC, "TotalPontos");
		Order orderNome = new Order(Sort.Direction.ASC, "Nome");
		
		return pessoaRepo.findAll(new Sort(orderTotalPontos, orderNome));
	}
	
	@PostMapping("/participantesporuser")
	public List<Pessoa> getParticipantes(@RequestBody  String username) {
		Usuario u = usuarioRepo.findByUsername(username);
		return pessoaRepo.findByCidadeInOrderByTotalPontosDescNome(u.getCidades());
	}
	
	@PostMapping(value = "/participantes")
	@ResponseBody
	public List<Pessoa> save(@RequestBody List<Pessoa> pessoas) {
		pessoas.forEach(p -> p.calculaTotalPontos());
		pessoaRepo.save(pessoas);
		return pessoas;
	}

	
}
