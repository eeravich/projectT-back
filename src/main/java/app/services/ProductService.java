package app.services;

import app.InvalidDataException;
import app.entities.pojos.ProductPojo;
import app.generated.jooq.tables.pojos.Product;
import app.generated.jooq.tables.pojos.RefProductComponent;
import app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.tools.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    public List<ProductPojo> getList() {
        List<ProductPojo> list = repository.getList();
        for (ProductPojo product : list) {
            Long productId = product.getProductId();
            product.setComponents(repository.getProductComponents(productId));
            product.setDiscounts(repository.getDiscountsByProductId(productId));
        }
        return list;
    }

    public ProductPojo getById(Long productId) {
        ProductPojo productPojo = repository.getById(productId);
        productPojo.setComponents(repository.getProductComponents(productId));
        productPojo.setDiscounts(repository.getDiscountsByProductId(productId));
        return productPojo;
    }

    public void createProduct(Product product) {
        validateCreateProduct(product);
        product.setProductId(repository.getNextProductId());
        repository.createProduct(product);
    }

    public void deleteProduct(Long productId) {
        //TODO: check refs and delete it too (if needed)
        repository.deleteProduct(productId);
    }

    public void editProduct(Product product) {
        validateCreateProduct(product);
        Product oldProduct = repository.getById(product.getProductId());
        deleteProduct(oldProduct.getProductId());
        repository.createProduct(product);
    }

    private void validateCreateProduct(Product product) {
        Map<String, String> errors = new HashMap<>();
        if (StringUtils.isBlank(product.getName())) {
            errors.put("name", "Name can't be null");
        }
        if (product.getPrice() == null) {
            errors.put("price", "Price can't be null");
        } else if (product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            errors.put("price", "Price can't be lower than zero");
        }
        if (product.getPortion() == null) {
            errors.put("portion", "Portion can't be null");
        } else if (product.getPortion() <= 0) {
            errors.put("portion", "Portion can't be less than 1");
        }
        if (product.getWeight() == null) {
            errors.put("weight", "Weight can't be null");
        } else if (product.getWeight() <= 0) {
            errors.put("weight", "Weight can't be less than 1");
        }
        if (product.getProductTypeId() == null) {
            errors.put("productType", "Product type can't be null");
        }

        if (!errors.isEmpty()) {
            throw new InvalidDataException(errors);
        }
    }

    private void addComponent(Long productId, Long componentId) {
        RefProductComponent ref = new RefProductComponent();
        ref.setComponentId(componentId);
        ref.setProductId(productId);
        repository.addComponent(ref);
    }

    private void deleteComponent(Long productId, Long componentId) {
        repository.deleteComponent(productId, componentId);
    }

    public void addComponents(Long productId, List<Long> componentIds) {
        for (Long componentId : componentIds) {
            addComponent(productId, componentId);
        }
    }

    public void deleteComponents(Long productId, List<Long> componentIds) {
        for (Long componentId : componentIds) {
            deleteComponent(productId, componentId);
        }
    }
}
