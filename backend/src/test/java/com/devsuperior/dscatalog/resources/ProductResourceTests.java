package com.devsuperior.dscatalog.resources;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper mapper;
	
	@MockBean
	private ProductService service;
	
	private Long existingId;
	private Long nonExistingId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> productPage;
	
	
	@BeforeEach
	void setUp() throws Exception{
		existingId=1L;
		nonExistingId=100L;
		
		productDTO = Factory.createProductDTO();
		productPage = new PageImpl<>(List.of(productDTO));
		
		Mockito.when(service.findAllPageable(ArgumentMatchers.any())).thenReturn(productPage);
		
		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
		
		Mockito.when(service.update(ArgumentMatchers.eq(existingId),ArgumentMatchers.any())).thenReturn(productDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(nonExistingId),ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
	}
	
	
	/*-----------------     FIND ALL TESTS     -------------------------------------------------------*/

	
	@Test
	public void findAllShouldReturnPage() throws Exception{
		ResultActions result =  
				mockMvc.perform(MockMvcRequestBuilders.get("/products")
						.accept(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	
	/*-----------------     FIND BY ID TESTS     -------------------------------------------------------*/

	@Test
	public void findByIdReturnNotFoundWhenInvalidId() throws Exception {
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",nonExistingId)
									.accept(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void findByIdShouldReturnProductWhenExistingId() throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/products/{id}",existingId)
				.accept(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
	}
	
	/*-----------------     INSERT TESTS     -------------------------------------------------------*/

	
	
	/*-----------------     UPDATE TESTS     -------------------------------------------------------*/

	@Test
	public void updateShouldReturnNotFoundWhenInvalidId() throws Exception {
		
		String jsonBody = mapper.writeValueAsString(productDTO);

		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}",nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void findByIdupdateShouldReturnProductDTOWhenExistingId() throws Exception {
		
		String jsonBody = mapper.writeValueAsString(productDTO);
		
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}",existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.name").exists());
		result.andExpect(MockMvcResultMatchers.jsonPath("$.description").exists());
	}
	/*-----------------     DELETE TESTS     -------------------------------------------------------*/

}
