package com.home.petstore.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/store/order")
public class OrderController {

  @Autowired OrderService orderService;

  @PostMapping
  public OrderDTO createOrder(@RequestBody OrderDTO orderDTO) {
    return orderService.createOrder(orderDTO);
  }

  @GetMapping
  public List<OrderDTO> getOrders(
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
    return orderService.getOrdersFromTo(from, to);
  }

  @GetMapping("/{orderId}")
  public OrderDTO getOrder(@PathVariable Long orderId) {
    return orderService.getOne(orderId);
  }

  @DeleteMapping("/{orderId}")
  public void deleteOrder(@PathVariable Long orderId) {
    orderService.deleteOne(orderId);
  }
}
