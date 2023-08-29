package com.amazonclone.Amazon_Backend.services;

public interface CartService {

	String deleteProductFromCart(Long cartId, Long productId);

	//Object updateProductInCarts(Long cartId, Long productId);

}
