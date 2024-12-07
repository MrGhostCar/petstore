package com.home.petstore.order;

import com.home.petstore.util.ExtendedModelMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
  @Autowired OrderRepository orderRepository;
  @Autowired ExtendedModelMapper modelMapper;

  public void createOrder(OrderDTO orderDAO) {
    OrderEntity orderEntity = modelMapper.map(orderDAO, OrderEntity.class);
    orderRepository.save(orderEntity);
  }

  public List<OrderDTO> getAll() {
    List<OrderEntity> orders = orderRepository.findAll();
    return modelMapper.mapList(orders, OrderDTO.class);
  }

  public OrderDTO getOne(Long orderId) {
    OrderEntity foundEntity =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(String.valueOf(orderId)));

    return modelMapper.map(foundEntity, OrderDTO.class);
  }
}
