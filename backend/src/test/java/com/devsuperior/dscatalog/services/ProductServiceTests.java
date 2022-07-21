package com.devsuperior.dscatalog.services;


import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;

	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private PageImpl<Product> page;
	private PageImpl<ProductDTO> pageDTO;
	private Product product;
	private ProductDTO productDTO;

	
	@BeforeEach
	void setUp() throws Exception{
		existingId =1L;
		nonExistingId = 1000L;
		dependentId = 4L;
		product = Factory.createProduct();
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(product));
		pageDTO = new PageImpl<>(List.of(productDTO));
		
		/*ArgumentMatchers passa um valor qualquer do tipo necessário para função sendo testada correr
		 * corretamente. 	Se método tiver sobrecargas é necessário indicar qual tipo de argumento deve
		 * ser passado*/
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		//Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);

		
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
		Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

		
		
		/*doNothing do mockito é colocado antes do when por serr usado funções que retornam VOID*/
		Mockito.doNothing().when(repository).deleteById(existingId);
		Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	

	
	
	
	 /*-----------------     FINDALL TESTS     -------------------------------------------------------*/


	@Test
	public void findAllPagedShouldReturnPage() {
		
		PageRequest pageRequest = PageRequest.of(0,10);
		Page<ProductDTO> result = service.findAllPaged(pageRequest);
		
		Assertions.assertNotNull(result);		
		
		/* Verifica se o método deleteById foi chamada dentro da ação do teste*/
		/* Mockito.times especifica quantas vezes o método deve ter sido chamado durante a ação do teste  */
		/* Mockito.never especifica que o método não deve ser chamado durante a ação do teste*/
		Mockito.verify(repository,Mockito.times(1)).findAll(pageRequest);
	}
	
	
	
	
	
	 /*-----------------     DELETE TESTS     -------------------------------------------------------*/
	
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
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(nonExistingId);
	}
		
	@Test 
	public void deleteShouldThrowEntityNotFoundExceptionWhenIdisDependent() {
		
		Assertions.assertThrows(DatabaseException.class,()->{
			service.delete(dependentId);
		});		
		
		Mockito.verify(repository,Mockito.times(1)).deleteById(dependentId);
	}



}
