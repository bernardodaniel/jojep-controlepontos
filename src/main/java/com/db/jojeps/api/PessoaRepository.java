package com.db.jojeps.api;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface PessoaRepository extends MongoRepository<Pessoa, String> {

	List<Pessoa> findByCidade(String cidade);

}
