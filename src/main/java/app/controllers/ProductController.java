package app.controllers;

import app.entities.pojos.ProductPojo;
import app.generated.jooq.tables.pojos.Product;
import app.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @GetMapping("/list")
    public List<ProductPojo> getList() {
        return service.getList();
    }

    @GetMapping("/{productId}")
    public ProductPojo getById(@PathVariable Long productId) {
        return service.getById(productId);
    }

    @PostMapping("/create")
    @Transactional
    public void createProduct(@RequestBody Product product) {
        service.createProduct(product);
    }

    @PutMapping("/edit")
    @Transactional
    public void editProduct(@RequestBody Product product) {
        service.editProduct(product);
    }

    @DeleteMapping("/delete/{productId}")
    @Transactional
    public void deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
    }

    @PutMapping("{productId}/addComponents")
    @Transactional
    public void addComponents(@PathVariable Long productId, @RequestBody List<Long> componentIds) {
        service.addComponents(productId, componentIds);
    }

    @PutMapping("{productId}/deleteComponents")
    @Transactional
    public void deleteComponent(@PathVariable Long productId, @RequestBody List<Long> componentIds) {
        service.deleteComponents(productId, componentIds);
    }
}
