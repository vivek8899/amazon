package com.amazonclone.Amazon_Backend.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.amazonclone.Amazon_Backend.dto.OrderDTO;
import com.amazonclone.Amazon_Backend.dto.OrderItemDTO;
import com.amazonclone.Amazon_Backend.dto.OrderResponse;
import com.amazonclone.Amazon_Backend.entities.Cart;
import com.amazonclone.Amazon_Backend.entities.CartItem;
import com.amazonclone.Amazon_Backend.entities.Order;
import com.amazonclone.Amazon_Backend.entities.OrderItem;
import com.amazonclone.Amazon_Backend.entities.Payment;
import com.amazonclone.Amazon_Backend.entities.Product;
import com.amazonclone.Amazon_Backend.exception.APIException;
import com.amazonclone.Amazon_Backend.exception.ResourceNotFoundException;
import com.amazonclone.Amazon_Backend.repository.CartRepository;
import com.amazonclone.Amazon_Backend.repository.OrderItemsRepository;
import com.amazonclone.Amazon_Backend.repository.OrderRepository;
import com.amazonclone.Amazon_Backend.repository.PaymentRepository;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class OrderServiceImpl implements OrderService
{

	

	@Autowired
	public ModelMapper modelMapper;
	
	@Autowired
	private OrderItemsRepository orderItemsrepo;

	
	@Autowired
	private CartRepository cartRepo;
	
		
	@Autowired
	private PaymentRepository paymentRepo;
	
	
	@Autowired
	private OrderRepository orderRepo;
	
	
	@Autowired
	private CartService cartService;
	
	
	@Override
	public OrderDTO placeOrder(Long cartId, String paymentMethod) {
		Cart cart = cartRepo.findById(cartId).get();

		if (cart == null) {
			throw new ResourceNotFoundException("Cart", "cartId", cartId);
		}

		Order order = new Order();
		
		

		order.setEmail(cart.getUser().getEmail());
		order.setOrderDate(LocalDate.now());

		order.setTotalAmount(cart.getTotalPrice());
		order.setOrderStatus("Order Accepted !");

		Payment payment = new Payment();
		payment.setOrder(order);
		payment.setPaymentMethod(paymentMethod);

		payment = paymentRepo.save(payment);

		order.setPayment(payment);

		Order savedOrder = orderRepo.save(order);

		List<CartItem> cartItems = cart.getCartItems();

		if (cartItems.size() == 0) {
			throw new APIException("Cart is empty");
		}

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cartItems) {
			OrderItem orderItem = new OrderItem();

			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setOrder(savedOrder);

			orderItems.add(orderItem);
		}

		orderItems = orderItemsrepo.saveAll(orderItems);

		cart.getCartItems().forEach(item -> {
			int quantity = item.getQuantity();

			Product product = item.getProduct();

			cartService.deleteProductFromCart(cartId, item.getProduct().getProductId());

			product.setQuantity(product.getQuantity() - quantity);
		});

		OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
		
		orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

		return orderDTO;
	}


	@Override
	public OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
				: Sort.by(sortBy).descending();

		Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

		Page<Order> pageOrders = orderRepo.findAll(pageDetails);

		List<Order> orders = pageOrders.getContent();

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}

		OrderResponse orderResponse = new OrderResponse();
		
		orderResponse.setContent(orderDTOs);
		orderResponse.setPageNumber(pageOrders.getNumber());
		orderResponse.setPageSize(pageOrders.getSize());
		orderResponse.setTotalElements(pageOrders.getTotalElements());
		orderResponse.setTotalPages(pageOrders.getTotalPages());
		orderResponse.setLastPage(pageOrders.isLast());
		
		return orderResponse;
	}


	@Override
	public List<OrderDTO> getOrdersByUser(String emailId) {
		List<Order> orders = orderRepo.findAllByEmail(emailId);

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());

		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the user with email: " + emailId);
		}

		return orderDTOs;
	}


	@Override
	public List<OrderDTO> getAllOrder() {
		

		List<Order> orders = orderRepo.findAll();

		List<OrderDTO> orderDTOs = orders.stream().map(order -> modelMapper.map(order, OrderDTO.class))
				.collect(Collectors.toList());
		
		if (orderDTOs.size() == 0) {
			throw new APIException("No orders placed yet by the users");
		}

		
		return orderDTOs;
	}


	@Override
	public OrderDTO getOrder(String emailId, Long orderId) {
		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		return modelMapper.map(order, OrderDTO.class);
	}


	@Override
	public OrderDTO updateOrder(String emailId, Long orderId, String orderStatus) {

		

		Order order = orderRepo.findOrderByEmailAndOrderId(emailId, orderId);

		if (order == null) {
			throw new ResourceNotFoundException("Order", "orderId", orderId);
		}

		order.setOrderStatus(orderStatus);
		orderRepo.save(order);

		return modelMapper.map(order, OrderDTO.class);
	}

}
