package com.amazonclone.Amazon_Backend.services;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.amazonclone.Amazon_Backend.dto.ProductDTO;
import com.amazonclone.Amazon_Backend.dto.ProductResponse;
import com.amazonclone.Amazon_Backend.entities.Product;

import jakarta.validation.Valid;

public interface ProductService {

	String deleteProduct(Long productId);

	ProductDTO addProduct(Long categoryId, @Valid Product product, MultipartFile image) throws IOException;

	List<ProductDTO> getAllProducts();

	ProductResponse getAllProductsByPage(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder);

	ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy,
			String sortOrder);

	ProductDTO updateProduct(Long productId, Product product);

	ProductDTO updateProductImage(Long productId, MultipartFile image)  throws IOException;

}
