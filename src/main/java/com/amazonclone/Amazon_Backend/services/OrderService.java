package com.amazonclone.Amazon_Backend.services;

import java.util.List;

import com.amazonclone.Amazon_Backend.dto.OrderDTO;
import com.amazonclone.Amazon_Backend.dto.OrderResponse;

public interface OrderService {

	OrderDTO placeOrder(Long cartId, String paymentMethod);

	OrderResponse getAllOrders(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	List<OrderDTO> getOrdersByUser(String emailId);

	List<OrderDTO> getAllOrder();

	OrderDTO getOrder(String emailId, Long orderId);

	OrderDTO updateOrder(String emailId, Long orderId, String orderStatus);

}
