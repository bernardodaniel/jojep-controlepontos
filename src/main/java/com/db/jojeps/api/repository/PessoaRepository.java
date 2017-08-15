package com.db.jojeps.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.db.jojeps.api.model.Pessoa;

public interface PessoaRepository extends MongoRepository<Pessoa, String> {

	List<Pessoa> findByCidadeInOrderByTotalPontosDescNome(List<String> cidades);

}
