package app.services.product;

import app.InvalidDataException;
import app.Utils;
import app.generated.projectT.model.ProductCreateRequest;
import app.generated.projectT.model.ProductEditRequest;
import app.generated.projectT.tables.pojos.*;
import app.mappers.product.ProductCreateMapper;
import app.mappers.product.ProductEditMapper;
import app.repository.RefComboProductRepository;
import app.repository.RefOrderProductRepository;
import app.repository.RefProductComponentRepository;
import app.repository.RefProductDiscountRepository;
import app.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductActionsService {
    private final ProductRepository productRepository;
    private final RefProductComponentRepository refProductComponentRepository;
    private final RefProductDiscountRepository refProductDiscountRepository;
    private final RefOrderProductRepository refOrderProductRepository;
    private final RefComboProductRepository refComboProductRepository;

    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteProduct(productId);
        refProductComponentRepository.deleteRefsByProductId(productId);
        refProductDiscountRepository.deleteRefByProductId(productId);
        refOrderProductRepository.deleteRefByProductId(productId);
        refComboProductRepository.deleteRefByProductId(productId);
    }

    @Transactional
    public Long createProduct(ProductCreateRequest productCreateRequest) {
        Long nextId = productRepository.getNextId();
        Product newProduct = ProductCreateMapper.MAPPER.map(productCreateRequest, nextId, Utils.now());
        productRepository.createProduct(newProduct);
        return nextId;
    }

    @Transactional
    public Long editProduct(ProductEditRequest productEditRequest) {
        Product product = productRepository.getProduct(productEditRequest.getId())
                .orElseThrow(() -> new InvalidDataException("product not found"));
        Long oldId = product.getId();
        if (product.getDeletedDatetime() != null) {
            throw new InvalidDataException("access denied (version not actual)");
        }

        List<RefProductComponent> actualProductComponentRefs = refProductComponentRepository.getActualRefsByProductId(oldId);
        List<RefProductDiscount> actualProductDiscountRefs = refProductDiscountRepository.getActualRefsByProductId(oldId);
        List<RefOrderProduct> actualOrderProductRefs = refOrderProductRepository.getActualRefsByProductId(oldId);
        List<RefComboProduct> actualComboProductRefs = refComboProductRepository.getActualRefsByProductId(oldId);


        deleteProduct(oldId);
        Long nextId = productRepository.getNextId();

        ProductEditMapper.MAPPER.map(product, nextId, Utils.now(), productEditRequest);
        productRepository.createProduct(product);

        refProductComponentRepository.createNewRefs(
                actualProductComponentRefs.stream()
                        .map(item -> ProductEditMapper.MAPPER.updateId(item, nextId))
                        .toList()
        );

        refProductDiscountRepository.createNewRefs(
                actualProductDiscountRefs.stream()
                        .map(item -> ProductEditMapper.MAPPER.updateId(item, nextId))
                        .toList()
        );

        refOrderProductRepository.createNewRefs(
                actualOrderProductRefs.stream()
                        .map(item -> ProductEditMapper.MAPPER.updateId(item, nextId))
                        .toList()
        );

        refComboProductRepository.createNewRefs(
                actualComboProductRefs.stream()
                        .map(item -> ProductEditMapper.MAPPER.updateId(item, nextId))
                        .toList()
        );

        return nextId;
    }
}
