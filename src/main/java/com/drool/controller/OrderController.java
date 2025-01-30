package com.drool.controller;

import com.drool.entities.Order;
import com.drool.service.OrderDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/discount")
public class OrderController {

    @Autowired
    private OrderDiscountService orderDiscountService;

    @PostMapping
    public ResponseEntity<Order> calculateDiscount(@RequestBody Order order) {
        return new ResponseEntity<>(orderDiscountService.calculateDiscount(order), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        List<Order> orders = orderDiscountService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderDiscountService.getOrderById(id);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }




}