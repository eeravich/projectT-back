package app.controllers.product;

import app.generated.projectT.api.ProductListApi;
import app.generated.projectT.model.ProductListItem;
import app.services.product.ProductListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductListController implements ProductListApi {
    private final ProductListService productListService;

    @Override
    public ResponseEntity<List<ProductListItem>> getProductList() {
        List<ProductListItem> productList = productListService.getList();
        return ResponseEntity.ok(productList);
    }
}
