package com.db.jojeps.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.db.jojeps.api.model.Usuario;

public interface UsuarioRepository extends MongoRepository<Usuario, String> {

	Usuario findByUsername(String username);

}
