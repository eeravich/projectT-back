package app.services.product;

import app.InvalidDataException;
import app.generated.projectT.model.ProductItem;
import app.generated.projectT.tables.pojos.Product;
import app.generated.projectT.tables.pojos.ProductType;
import app.mappers.product.ProductViewMapper;
import app.repository.product.ProductDictionaryRepository;
import app.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductViewService {
    private final ProductRepository productRepository;
    private final ProductDictionaryRepository productDictionaryRepository;

    public ProductItem getProduct(Long productId) {
        Product product = productRepository.getProduct(productId)
                .orElseThrow(() -> new InvalidDataException("product not found"));
        List<ProductType> productTypeList = productDictionaryRepository.getProductTypeList();

        if (product.getDeletedDatetime() != null) {
            throw new InvalidDataException("access denied (version is not actual)");
        }

        return ProductViewMapper.MAPPER.map(product, productTypeList);
    }
}
