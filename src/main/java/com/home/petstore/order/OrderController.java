package com.home.petstore.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("/store")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/order")
    public void createOrder (@RequestBody OrderDTO orderDAO) {
        orderService.createOrder(orderDAO);
    }

    @GetMapping("/order")
    public List<OrderDTO> getOrders() {
        return orderService.getAll();
    }

}
