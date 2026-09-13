package cl.duoc.cloudstore.order.controller;

import cl.duoc.cloudstore.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody OrderDto dto) {
        return ResponseEntity.ok(service.createAndProcessOrder(dto.productId(), dto.amount()));
    }
}

record OrderDto(Long productId, Double amount) {}