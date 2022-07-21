package com.devsuperior.dscatalog.tests;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class Factory {

	public static Product createProduct() {
		Product product = new Product(1L,"Phone","Good Phone",800.0,"http://img.com/img.png",Instant.parse("2020-07-03T03:00:00Z"));
		product.getCategories().add(new Category(2L,"Eletronics"));
		return product;
	}
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product,product.getCategories());
	}
	public static Category createCategory() {
		Category category = new Category(1L,"CATEGORY_TEST");
		return category;
	}
	public static CategoryDTO createCategoryDTO() {
		Category category = createCategory();
		return new CategoryDTO(category);
	}
}
