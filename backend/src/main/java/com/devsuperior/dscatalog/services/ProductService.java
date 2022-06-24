package com.devsuperior.dscatalog.services;


import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

/*Anotation registra a classe como um componente que participa da injeção de dependencias automatizado do spring,
 * quem vai gerenciar as instancias da injeção de dependencia será o próprio spring
 * */
@Service
public class ProductService {

	@Autowired
	private ProductRepository repository ;
	@Autowired
	private CategoryRepository category_repository;
	
	/*@Transactional(readOnly = true)        ANTIGO
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
	}*/
	
	@Transactional(readOnly = true)        /*  NOVO       */
	public Page<ProductDTO> findAllPaged(PageRequest request){
		Page<Product> list = repository.findAll(request); 
		return list.map(x->new ProductDTO(x));
	}
	
	@Transactional(readOnly = true)        /*  NOVO       */
	public Page<ProductDTO> findAllPageable(Pageable pageable){
		Page<Product> list = repository.findAll(pageable); 
		return list.map(x->new ProductDTO(x));
	}
	
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id){
		Optional<Product> obj = repository.findById(id);
		Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("ENTITY NOT FOUND") );
		return new ProductDTO(entity,entity.getCategories());
		
	}
	@Transactional(readOnly = true)
	public ProductDTO insert(ProductDTO dto) {
		Product product = new Product();
		copyDtoToEntity(dto,product);
		product = repository.save(product);
		return new ProductDTO(product);
	}

	@Transactional
	public ProductDTO update(Long id,ProductDTO dto) {
		try {
			Product product = repository.getReferenceById(id);
			copyDtoToEntity(dto,product);
			product = repository.save(product);
			return new ProductDTO(product);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found " + id);
		}
		catch(DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity Violation");
		}
	}
	
	private void copyDtoToEntity(ProductDTO dto, Product product) {
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setDate(dto.getDate());
		product.setImgUrl(dto.getImgUrl());
		product.setPrice(dto.getPrice());
		
		product.getCategories().clear();
		for(CategoryDTO catDto : dto.getCategories()) {
			Category cat = category_repository.getReferenceById(catDto.getId());
			product.getCategories().add(cat);
		}		
	}
	
	
}
