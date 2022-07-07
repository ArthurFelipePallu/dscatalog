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
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;


/*Anotation registra a classe como um componente que participa da injeção de dependencias automatizado do spring,
 * quem vai gerenciar as instancias da injeção de dependencia será o próprio spring
 * */
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository ;
	
	/*@Transactional(readOnly = true)        ANTIGO
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
	}*/
	
	@Transactional(readOnly = true)        /*  NOVO       */
	public Page<CategoryDTO> findAllPaged(PageRequest request){
		Page<Category> list = repository.findAll(request); 
		return list.map(x->new CategoryDTO(x));
	}
	
	@Transactional(readOnly = true)        /*  NOVO       */
	public Page<CategoryDTO> findAllPageable(Pageable pageable){
		Page<Category> list = repository.findAll(pageable); 
		return list.map(x->new CategoryDTO(x));
	}
	
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("ENTITY NOT FOUND") );
		return new CategoryDTO(entity);
		
	}
	@Transactional(readOnly = true)
	public CategoryDTO insert(CategoryDTO dto) {
		Category category = new Category();
		category.setName(dto.getName());
		category = repository.save(category);
		return new CategoryDTO(category);
	}
	@Transactional
	public CategoryDTO update(Long id,CategoryDTO dto) {
		try {
			Category category = repository.getReferenceById(id);
			category.setName(dto.getName());
			category = repository.save(category);
			return new CategoryDTO(category);
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
}
