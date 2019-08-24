package com.db.jojeps.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.db.jojeps.api.model.Credenciais;
import com.db.jojeps.api.model.Papel;
import com.db.jojeps.api.model.Pessoa;
import com.db.jojeps.api.model.Usuario;
import com.db.jojeps.api.repository.UsuarioRepository;

@RestController
public class UsuarioResource {

	@Autowired
	private UsuarioRepository usuarioRepo;
	
	@RequestMapping("/user")
	public Principal user(Principal user) {
		System.out.println( user );
		return user;
	}
	
	@PostMapping(value = "/alterarsenha", produces = MediaType.TEXT_PLAIN_VALUE)
	public String alterarSenha(@RequestBody Credenciais credenciais) {
		Usuario u = usuarioRepo.findByUsername(credenciais.usuario);
		
		if (!credenciais.senhaAtual.equals(u.getPassword())) {
			throw new RuntimeException("A senha atual não é igual à senha cadastrada para o usuário");
		}
		
		if (!credenciais.novaSenha.equals(credenciais.confirmeSenha)) {
			throw new RuntimeException("A confirmação de senha não é igual à nova senha digitada");
		}
		
		u.setPassword(credenciais.novaSenha);
		
		usuarioRepo.save(u);
		
		return "Senha alterada com sucesso";
	}
	
	@GetMapping("/admin/import/user")
	public String importar() {
		File file = new File("usuarios_2019.csv");
		
		String expiraEm = "10/09/2025 00:00";
		usuarioRepo.deleteAll();
		
		Usuario su = new Usuario();
		su.setUsername("admin");
		su.setPassword("ops.!@#");
		su.addPapel(new Papel("su"));
		su.setExpiraEm("09/09/2025 00:00");
		
		Usuario admin = new Usuario();
		admin.setUsername("administrador");
		admin.setPassword("admjojeps");
		admin.addPapel(new Papel("admin"));
		admin.setExpiraEm(expiraEm);
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String st;
			
			while((st = br.readLine()) != null){
				String[] colunas = st.split(";");
				String cidade = colunas[0].trim();
				
				Usuario u = new Usuario();
				u.setUsername(colunas[1].trim());
				u.setPassword(colunas[2].trim());
				u.addPapel(new Papel("coordenador"));
				u.addCidade(cidade);
				u.setExpiraEm(expiraEm);
				
				usuarioRepo.save(u);
				System.out.println(u);
				
				su.addCidade(cidade);
				admin.addCidade(cidade);
			}
			
			usuarioRepo.save(su);
			System.out.println(su);
			usuarioRepo.save(admin);
			System.out.println(admin);
			
		} catch (IOException e) {
			e.printStackTrace();
			return "erro: " + e.getMessage();
		}
		
		return "sucesso";
	}
	
	@GetMapping("/admin/export/user")
	public List<Usuario> export() {
		return usuarioRepo.findAll();
	}
	
}
