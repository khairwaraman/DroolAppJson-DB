package com.drool.service;

import com.drool.entities.Order;
import com.drool.repositories.OrderRepository;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDiscountService {

    @Autowired
    private RuleService ruleService;

    @Autowired
    private OrderRepository orderRepository;

    public Order calculateDiscount(Order order) {
        KieSession kieSession = ruleService.createKieSession();
        kieSession.insert(order);
        kieSession.fireAllRules();
        kieSession.dispose();
        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

}