package com.generation.farmacia.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> buscarTodosProdutos() {
		
		return ResponseEntity.ok(produtoRepository.findAll());
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> buscarPorId(@PathVariable Long id) {

		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

	}
	
	@GetMapping("/ordem-menor-preco")
	public ResponseEntity<List<Produto>> listarProdutosOrdenadosMenorPreco() {
		
		List<Produto> produtos = produtoRepository.findAllByOrderByPrecoAsc();
		
		if (produtos.isEmpty())
			return ResponseEntity.noContent().build();
		
		return ResponseEntity.ok(produtos);
		
	}
	
	@PostMapping("/cadastrar")
	public ResponseEntity<Produto> cadastrarProduto(@Valid @RequestBody Produto produto) {
		
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(produtoRepository.save(produto));
		
	}
	
	@PutMapping("/atualizar")
	public ResponseEntity<Produto> atualizarProduto(@Valid @RequestBody Produto produto) {
		
		return produtoRepository.findById(produto.getId())
				.map(resposta -> ResponseEntity.status(HttpStatus.OK)
						.body(produtoRepository.save(produto)))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
		
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/deletar/{id}")
	public void deletarProduto(@PathVariable Long id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if (produto.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		produtoRepository.deleteById(id);
		
	}
	

}









