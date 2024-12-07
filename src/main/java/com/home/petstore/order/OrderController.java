package com.home.petstore.order;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/store/order")
public class OrderController {

  @Autowired OrderService orderService;

  @PostMapping
  public void createOrder(@RequestBody OrderDTO orderDTO) {
    orderService.createOrder(orderDTO);
  }

  @GetMapping
  public List<OrderDTO> getOrders() { // TODO
    return orderService.getAll();
  }

  @GetMapping("/{orderId}")
  public OrderDTO getOrder(@PathVariable Long orderId) {
    return orderService.getOne(orderId);
  }
}
