package com.amazonclone.Amazon_Backend.services;

import java.util.List;

import com.amazonclone.Amazon_Backend.dto.CartDTO;

public interface CartService {

	String deleteProductFromCart(Long cartId, Long productId);

	void updateProductInCarts(Long cartId, Long productId);

	CartDTO addProductToCart(Long cartId, Long productId, Integer quantity);

	List<CartDTO> getAllCarts();

	CartDTO getCart(Long cartId);

	CartDTO updateProductQuantityInCart(Long cartId, Long productId, Integer quantity);

	

}
