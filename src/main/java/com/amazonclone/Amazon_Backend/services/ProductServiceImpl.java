package com.amazonclone.Amazon_Backend.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonclone.Amazon_Backend.entities.Cart;
import com.amazonclone.Amazon_Backend.entities.Product;
import com.amazonclone.Amazon_Backend.exception.ResourceNotFoundException;
import com.amazonclone.Amazon_Backend.repository.CartRepository;
import com.amazonclone.Amazon_Backend.repository.ProductRepository;

import jakarta.transaction.Transactional;


@Transactional
@Service
public class ProductServiceImpl  implements ProductService{

	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	

	@Autowired
	private CartRepository cartRepo;

	@Autowired
	private CartService cartService;
	
	@Override
	public String deleteProduct(Long productId) {
	

		Product product = productRepo.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

		List<Cart> carts = cartRepo.findCartsByProductId(productId);

		carts.forEach(cart -> cartService.deleteProductFromCart(cart.getCartId(), productId));

		productRepo.delete(product);

		return "Product with productId: " + productId + " deleted successfully !!!";
		
	}

	
}
