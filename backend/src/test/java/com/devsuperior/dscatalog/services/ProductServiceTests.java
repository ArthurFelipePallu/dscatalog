package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;

	@BeforeEach
	void setUp() throws Exception{
		existingId =1L;
		nonExistingId = 1000L;
		
		
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
	}
	
	
	@Test 
	public void deleteShouldDoNothingWhenIdExists() {
		
		Assertions.assertDoesNotThrow(()->{
			service.delete(existingId);
		});		
		/* Verifica se o método deleteById foi chamada dentro da ação do teste*/
		/* Mockito.times especifica quantas vezes o método deve ter sido chamado durante a ação do teste  */
		/* Mockito.never especifica que o método não deve ser chamado durante a ação do teste*/
		Mockito.verify(repository,Mockito.times(1)).deleteById(existingId);
	}
	
	@Test 
	public void deleteShouldThrowExceptionWhenIdDoesNotExists() {
		
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(nonExistingId);
		});		
		
		
	}
	
}
