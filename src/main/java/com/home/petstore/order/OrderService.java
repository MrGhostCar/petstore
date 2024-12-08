package com.home.petstore.order;

import com.home.petstore.util.ExtendedModelMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
  @Autowired OrderRepository orderRepository;
  @Autowired ExtendedModelMapper modelMapper;

  public void createOrder(OrderDTO orderDAO) {
    OrderEntity orderEntity = modelMapper.map(orderDAO, OrderEntity.class);
    orderEntity.setId(null);
    orderRepository.save(orderEntity);
  }

  public List<OrderDTO> getOrdersFromTo(LocalDateTime from, LocalDateTime to) {
    List<OrderEntity> foundOrders = orderRepository.findAllByShipDateBetween(from, to);
    return modelMapper.mapList(foundOrders, OrderDTO.class);
  }

  public OrderDTO getOne(Long orderId) {
    OrderEntity foundEntity =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new EntityNotFoundException(String.valueOf(orderId)));

    return modelMapper.map(foundEntity, OrderDTO.class);
  }

    public void deleteOne(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
