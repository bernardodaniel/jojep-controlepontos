package com.db.jojeps.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.jojeps.api.model.Papel;
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
	
	@GetMapping("/admin/import/user")
	public String importar() {
		File file = new File("usuarios.csv");
		
		usuarioRepo.deleteAll();
		
		Usuario su = new Usuario();
		su.setUsername("admin");
		su.setPassword("123");
		su.addPapel(new Papel("su"));
		
		Usuario admin = new Usuario();
		admin.setUsername("administrador");
		admin.setPassword("admjojeps");
		admin.addPapel(new Papel("admin"));
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			
			String st;
			
			while((st = br.readLine()) != null){
				String[] colunas = st.split(";");
				String cidade = colunas[0];
				
				Usuario u = new Usuario();
				u.setUsername(colunas[1]);
				u.setPassword("jojeps2017");
				u.addPapel(new Papel("coordenador"));
				u.addCidade(cidade);
				
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
	
}
