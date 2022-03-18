package app.mappers.product;

import app.generated.projectT.model.ProductItem;
import app.generated.projectT.tables.pojos.Product;
import app.generated.projectT.tables.pojos.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductViewMapper {
    ProductViewMapper MAPPER = Mappers.getMapper(ProductViewMapper.class);

    @Mapping(target = "productTypeName", expression = "java(getProductTypeName(product.getProductTypeId(), productTypeList))")
    ProductItem map(Product product, List<ProductType> productTypeList);

    default String getProductTypeName(Integer productTypeId, List<ProductType> productTypeList) {
        return productTypeList.stream()
                .filter(item -> item.getId().equals(productTypeId))
                .map(ProductType::getName)
                .findAny()
                .orElse(null);
    }
}
