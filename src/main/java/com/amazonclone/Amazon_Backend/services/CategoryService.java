package com.amazonclone.Amazon_Backend.services;

import java.util.List;

import com.amazonclone.Amazon_Backend.dto.CategoryDTO;
import com.amazonclone.Amazon_Backend.entities.Category;

public interface CategoryService {

	CategoryDTO createCategory( Category category);

	List<CategoryDTO> getCategories();

	CategoryDTO updateCategory(Category category, Long categoryId);

	String deleteCategory(Long categoryId);

}
