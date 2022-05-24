package com.devsuperior.dscatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;

/*Anotation registra a classe como um componente que participa da injeção de dependencias automatizado do spring,
 * quem vai gerenciar as instancias da injeção de dependencia será o próprio spring
 * */
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository ;
	
	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		List<Category> list = repository.findAll();
		return list.stream().map(x->new CategoryDTO(x)).collect(Collectors.toList());
	}
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id){
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new EntityNotFoundException("ENTITY NOT FOUND") );
		return new CategoryDTO(entity);
		
	}
	@Transactional(readOnly = true)
	public CategoryDTO insert(CategoryDTO dto) {
		Category category = new Category();
		category.setName(dto.getName());
		category = repository.save(category);
		return new CategoryDTO(category);
	}
}
