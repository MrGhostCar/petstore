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

  public OrderDTO createOrder(OrderDTO orderDTO) {
    OrderEntity orderEntity = modelMapper.map(orderDTO, OrderEntity.class);
    orderEntity.setId(null);
    OrderEntity savedEntity = orderRepository.save(orderEntity);
      return modelMapper.map(savedEntity, OrderDTO.class);
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
