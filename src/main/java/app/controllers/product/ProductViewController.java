package app.controllers.product;

import app.generated.projectT.api.ProductViewApi;
import app.generated.projectT.model.ProductItem;
import app.services.product.ProductViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductViewController implements ProductViewApi {
    private final ProductViewService productViewService;

    @Override
    public ResponseEntity<ProductItem> getProductItem(Long productId) {
        ProductItem product = productViewService.getProduct(productId);
        return ResponseEntity.ok(product);
    }
}
