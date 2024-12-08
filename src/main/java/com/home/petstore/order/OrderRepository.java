package com.home.petstore.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

  List<OrderEntity> findAllByShipDateBetween(
      LocalDateTime startDate, LocalDateTime endDate);

}
