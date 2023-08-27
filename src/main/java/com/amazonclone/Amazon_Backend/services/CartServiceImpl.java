package com.amazonclone.Amazon_Backend.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonclone.Amazon_Backend.entities.Cart;
import com.amazonclone.Amazon_Backend.entities.CartItem;
import com.amazonclone.Amazon_Backend.entities.Product;
import com.amazonclone.Amazon_Backend.exception.ResourceNotFoundException;
import com.amazonclone.Amazon_Backend.repository.CartItemRepository;
import com.amazonclone.Amazon_Backend.repository.CartRepository;

import jakarta.transaction.Transactional;




@Transactional
@Service
public class CartServiceImpl  implements CartService{

	
	@Autowired
	private CartRepository cartRepo;
	

	@Autowired
	private CartItemRepository cartItemRepo;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public String deleteProductFromCart(Long cartId, Long productId) {
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

		CartItem cartItem = cartItemRepo.findCartItemByProductIdAndCartId(cartId, productId);

		if (cartItem == null) {
			throw new ResourceNotFoundException("Product", "productId", productId);
		}

		cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));

		Product product = cartItem.getProduct();
		product.setQuantity(product.getQuantity() + cartItem.getQuantity());

		cartItemRepo.deleteCartItemByProductIdAndCartId(cartId, productId);

		return "Product " + cartItem.getProduct().getProductName() + " removed from the cart !!!";
	}

}
