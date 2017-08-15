package com.db.jojeps;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.jojeps.api.service.UserServiceImpl;

@SpringBootApplication
public class JojepsControlepontosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JojepsControlepontosApiApplication.class, args);
	}
	

	@Configuration
	@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		
		@Autowired
		private UserServiceImpl usarioService;

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.httpBasic().and()
			.authorizeRequests()
				.antMatchers("/index.html", "/home.html", "/login.html", "/", "/img/**", "/css/**", "/js/**", "/admin/import/**").permitAll()
				.anyRequest().authenticated().and()
			.csrf()
				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		}
		
		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//			auth.inMemoryAuthentication().withUser("admin").password("123").roles("SU");
//			
//			auth.inMemoryAuthentication().withUser("cristiane.wencel").password("jojeps2017").roles("Campo Mourão");
//			auth.inMemoryAuthentication().withUser("luis.guilherme").password("jojeps2017").roles("Cascavel");
//			auth.inMemoryAuthentication().withUser("diego.ferreira").password("jojeps2017").roles("Cianorte");
//			auth.inMemoryAuthentication().withUser("marcio").password("jojeps2017").roles("Dois vizinhos");
//			auth.inMemoryAuthentication().withUser("bruno.elias").password("jojeps2017").roles("Foz do Iguaçu");
//			auth.inMemoryAuthentication().withUser("eduardo").password("jojeps2017").roles("Guarapuava");
//			auth.inMemoryAuthentication().withUser("alexandre").password("jojeps2017").roles("Londrina");
//			auth.inMemoryAuthentication().withUser("douglas.alexandre").password("jojeps2017").roles("Marechal");
//			auth.inMemoryAuthentication().withUser("bruna").password("jojeps2017").roles("Maringá");
//			auth.inMemoryAuthentication().withUser("mirian").password("jojeps2017").roles("Mercedes");
//			auth.inMemoryAuthentication().withUser("alan").password("jojeps2017").roles("Palotina");
//			auth.inMemoryAuthentication().withUser("rodolfo").password("jojeps2017").roles("Paranavaí");
//			auth.inMemoryAuthentication().withUser("rian").password("jojeps2017").roles("Pato Bragado");
//			auth.inMemoryAuthentication().withUser("henrique").password("jojeps2017").roles("Pato branco");
//			auth.inMemoryAuthentication().withUser("diego bonaldo").password("jojeps2017").roles("Toledo");
//			auth.inMemoryAuthentication().withUser("kenny").password("jojeps2017").roles("Umuarama");
			
			auth.userDetailsService(usarioService);

		}
	}

}
