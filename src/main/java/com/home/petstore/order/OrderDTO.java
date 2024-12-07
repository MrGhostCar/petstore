package com.home.petstore.order;

import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class OrderDTO {

    Long id;
    Long petId;
    Integer quantity;
    Timestamp shipDate;
    OrderStatus status;
    Boolean complete;

}
