package app.mappers.product;

import app.generated.projectT.model.ProductListItem;
import app.generated.projectT.tables.pojos.Product;
import app.generated.projectT.tables.pojos.ProductType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductListMapper {
    ProductListMapper MAPPER = Mappers.getMapper(ProductListMapper.class);

    @Mapping(target = "productTypeName", expression = "java(getProductTypeName(product.getProductTypeId(), productTypeList))")
    ProductListItem map(Product product, List<ProductType> productTypeList);


    default String getProductTypeName(Integer productTypeId, List<ProductType> productTypeList) {
        return productTypeList.stream()
                .filter(item -> item.getId().equals(productTypeId))
                .map(ProductType::getName)
                .findAny()
                .orElse(null);
    }
}
