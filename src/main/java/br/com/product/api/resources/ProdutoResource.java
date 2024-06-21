package br.com.product.api.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.product.api.models.Produto;
import br.com.product.api.repositories.ProdutoRepository;

@RestController
@RequestMapping(path="/produto")
public class ProdutoResource {
	
	private ProdutoRepository produtoRepository;
	
	public ProdutoResource(ProdutoRepository produtoRepository) {
		super();
		this.produtoRepository = produtoRepository;
	}

	@PostMapping
	public ResponseEntity<Produto> save(@RequestBody Produto produto){
		produtoRepository.save(produto);
		return new ResponseEntity<Produto>(produto, HttpStatus.CREATED);
	}

	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){
		List<Produto> produtos = new ArrayList<>();
		produtos = produtoRepository.findAll();
		return new ResponseEntity<>(produtos, HttpStatus.OK);
	}
	
	@GetMapping(path="/{codigo}")
	public ResponseEntity<Optional<Produto>> getByCodigo(@PathVariable Integer codigo){
		Optional<Produto> produto;
		produto = produtoRepository.findById(codigo);
			
		if(!produto.isEmpty()) {
			return new ResponseEntity<Optional<Produto>>(produto, HttpStatus.OK);
		}
		else
			return new ResponseEntity<Optional<Produto>>(HttpStatus.NOT_FOUND);
	}
		
	@DeleteMapping(path="/{codigo}")
	public ResponseEntity<Optional<Produto>> deleteByCodigo(@PathVariable Integer codigo){
		try {
			produtoRepository.deleteById(codigo);
			return new ResponseEntity<Optional<Produto>>(HttpStatus.OK);
		} catch (NoSuchElementException exc) {
			return new ResponseEntity<Optional<Produto>>(HttpStatus.NOT_FOUND);
		} catch (Exception exc) {
			return new ResponseEntity<Optional<Produto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping(value="/{codigo}")
	public ResponseEntity<Produto> update(@PathVariable Integer codigo, @RequestBody Produto newProduto){
		try {
			return produtoRepository.findById(codigo)
					.map(produto -> {
						produto.setNome(newProduto.getNome());
						produto.setPreco(newProduto.getPreco());
						produto.setMarca(newProduto.getMarca());
						Produto produtoUpdated = produtoRepository.save(produto);
						return ResponseEntity.ok().body(produtoUpdated);
					}).orElse(ResponseEntity.notFound().build());
		} catch (NoSuchElementException exc) {
			return new ResponseEntity<Produto>(HttpStatus.NOT_FOUND);
		} catch (Exception ex) {
			return new ResponseEntity<Produto>(HttpStatus.BAD_REQUEST);
		}	
	}
}
