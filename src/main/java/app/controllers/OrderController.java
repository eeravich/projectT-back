package app.controllers;

import app.entities.enums.OrderStatuses;
import app.entities.enums.Roles;
import app.entities.pojos.OrderPojo;
import app.generated.jooq.tables.pojos.Order;
import app.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @GetMapping("/list")
    @Secured({Roles.Fields.ROLE_MANAGER, Roles.Fields.ROLE_ADMIN})
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

    @PutMapping("/{orderId}/cancel")
    @Transactional
    public void cancelOrder(@PathVariable Long orderId) {
        service.changeStatus(orderId, OrderStatuses.CANCELED.getId());
    }

    @PutMapping("/{orderId}/submit")
    @Transactional
    public void submitOrder(@PathVariable Long orderId) {
        service.changeStatus(orderId, OrderStatuses.SUBMITTED.getId());
    }
}
