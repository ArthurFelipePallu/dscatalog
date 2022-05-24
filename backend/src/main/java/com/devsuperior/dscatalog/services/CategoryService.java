package com.devsuperior.dscatalog.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;

/*Anotation registra a classe como um componente que participa da injeção de dependencias automatizado do spring,
 * quem vai gerenciar as instancias da injeção de dependencia será o próprio spring
 * */
@Service
public class CategoryService {

	@Autowired
	private CategoryRepository repository ;
	
	@Transactional(readOnly = true)
	public List<Category> findAll(){
		return repository.findAll();
	}
}
