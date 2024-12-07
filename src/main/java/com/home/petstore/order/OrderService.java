package com.home.petstore.order;

import com.home.petstore.util.ExtendedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ExtendedModelMapper modelMapper;

    public void createOrder(OrderDTO orderDAO) {
        OrderEntity orderEntity = modelMapper.map(orderDAO, OrderEntity.class);
        orderRepository.save(orderEntity);
    }

    public List<OrderDTO> getAll() {
        List<OrderEntity> orders = orderRepository.findAll();
        return modelMapper.mapList(orders, OrderDTO.class);
    }
}
