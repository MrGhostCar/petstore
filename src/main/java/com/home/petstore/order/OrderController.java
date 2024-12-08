package com.home.petstore.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
  public ResponseEntity<OrderDTO> getOrder(@PathVariable Long orderId) {
    try {
      return new ResponseEntity<>(orderService.getOne(orderId), HttpStatus.OK);
    } catch (EntityNotFoundException e) {
      return new ResponseEntity<OrderDTO>(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{orderId}")
  public void deleteOrder(@PathVariable Long orderId) {
    orderService.deleteOne(orderId);
  }

  @PatchMapping(path = "/{orderId}", consumes = "application/json-patch+json")
  public ResponseEntity<OrderDTO> updateCustomer(@PathVariable Long orderId, @RequestBody JsonPatch patch) {
    try {
      OrderDTO foundOrderDTO = orderService.getOne(orderId);
      OrderDTO patchedOrder = orderService.applyPatchToOrder(patch, foundOrderDTO);
      OrderDTO newOrder = orderService.saveOrder(patchedOrder);
      return ResponseEntity.ok(newOrder);
    } catch (JsonPatchException | JsonProcessingException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    } catch (EntityNotFoundException e) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
  }
}
