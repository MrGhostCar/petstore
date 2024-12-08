package com.home.petstore.order;

import com.home.petstore.pet.PetEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "store_order")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name = "pet_id")
    private PetEntity petId;

    private Integer quantity;
    private LocalDateTime shipDate;
    private OrderStatus status;
    private Boolean complete;

}
