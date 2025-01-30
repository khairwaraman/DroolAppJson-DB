package com.drool.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardType;
    private double amount;
    private double discount;
    private double finalAmount;

    // Getters and Setters
}