package app.controllers;

import app.entities.pojos.OrderPojo;
import app.generated.jooq.tables.pojos.Order;
import app.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping("/list")
    public List<OrderPojo> getList() {
        return service.getList();
    }

    @GetMapping("/{orderId}")
    public OrderPojo getById(@PathVariable Long orderId) {
        return service.getById(orderId);
    }

    @PostMapping("/create")
    @Transactional
    public void createOrder(@RequestBody Order order) {
        service.createOrder(order);
    }

    @PutMapping("/edit")
    @Transactional
    public void editOrder(@RequestBody Order order) {
        service.editOrder(order);
    }

    @DeleteMapping("/delete/{orderId}")
    @Transactional
    public void deleteOrder(@PathVariable Long orderId) {
        service.deleteOrder(orderId);
    }
}
