package br.com.product.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org .springframework.stereotype.Repository;

import br.com.product.api.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

}
