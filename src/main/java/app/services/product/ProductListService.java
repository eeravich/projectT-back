package app.services.product;

import app.generated.projectT.model.ProductListItem;
import app.generated.projectT.tables.pojos.Product;
import app.generated.projectT.tables.pojos.ProductType;
import app.mappers.product.ProductListMapper;
import app.repository.product.ProductDictionaryRepository;
import app.repository.product.ProductListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductListService {
    private final ProductListRepository productListRepository;
    private final ProductDictionaryRepository productDictionaryRepository;

    public List<ProductListItem> getList() {
        List<Product> productList = productListRepository.getList();
        List<ProductType> productTypeList = productDictionaryRepository.getProductTypeList();

        return productList.stream()
                .map(item -> ProductListMapper.MAPPER.map(item, productTypeList))
                .toList();
    }
}
