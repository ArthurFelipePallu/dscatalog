package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@DataJpaTest /* Carrega componentes e infraestrutura do SpringDataJPA sem os controladores e services */
public class ProductRepositoryTests {
	
	@Autowired /* Instância real de ProductRepository sendo injetada 
	 			para realizar teste , Seed do Banco de dados em import.sql
	 			 por estar carregando todo o contexto da JPA */
	private ProductRepository repository;
	
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
 	
	@BeforeEach
	void setUp() throws Exception{
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
	}
	
	@Test
	public void findByIdShouldReturnProductObjectWhenIdExists() {
		Optional<Product> product = repository.findById(existingId);
		
		Assertions.assertNotNull(product.isPresent());		
	}
	
	@Test
	public void findByIdShouldReturnNullProductObjectWhenIdDoesNotExists() {
		Optional<Product> product = repository.findById(nonExistingId);
		
		Assertions.assertFalse(product.isPresent());		
	}
	
	/*
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
	
		Assertions.assertThrowsExactly(ResourceNotFoundException.class,()->{
			repository.findById(nonExistingId);
		});
		
	}
	*/
	
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		
		Product product = Factory.createProduct();
		product.setId(null);
		
		product = repository.save(product);
		
		Assertions.assertNotNull( product.getId());
		Assertions.assertEquals(product.getId(), countTotalProducts + 1);
		
	}
	
	@Test
	public void deleteShouldDeleteObjWhenIdExists() {
		
		repository.deleteById(existingId);
		
		Optional<Product> result =  repository.findById(existingId);
		
		/* isPresent retorna se existe um objeto dentro da variavel Optional*/
		Assertions.assertFalse(result.isPresent()); 
	}
	
	@Test
	public void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrowsExactly(EmptyResultDataAccessException.class, ()->{
			
			repository.deleteById(nonExistingId);
			
		});		
	}
}
