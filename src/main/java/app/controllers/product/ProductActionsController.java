package app.controllers.product;

import app.generated.projectT.api.ProductActionsApi;
import app.generated.projectT.model.ProductCreateRequest;
import app.generated.projectT.model.ProductEditRequest;
import app.services.product.ProductActionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductActionsController implements ProductActionsApi {
    private final ProductActionsService productActionsService;

    @Override
    public ResponseEntity<Long> createProduct(ProductCreateRequest productCreateRequest) {
        Long newProductId = productActionsService.createProduct(productCreateRequest);
        return ResponseEntity.ok(newProductId);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long productId) {
        productActionsService.deleteProduct(productId);
        return null;
    }

    @Override
    public ResponseEntity<Long> editProduct(ProductEditRequest productEditRequest) {
        Long newProductId = productActionsService.editProduct(productEditRequest);
        return ResponseEntity.ok(newProductId);
    }
}
