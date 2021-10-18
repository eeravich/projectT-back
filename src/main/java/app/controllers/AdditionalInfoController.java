package app.controllers;

import app.generated.jooq.tables.pojos.OrderStatus;
import app.generated.jooq.tables.pojos.ProductType;
import app.repository.OrderRepository;
import app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class AdditionalInfoController {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @GetMapping("/orderStatuses")
    public List<OrderStatus> getOrderStatuses() {
        return orderRepository.getOrderStatuses();
    }

    @GetMapping("/productTypes")
    public List<ProductType> getProductTypes() {
        return productRepository.getProductTypes();
    }
}
