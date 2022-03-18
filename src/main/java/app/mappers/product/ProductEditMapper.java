package app.mappers.product;

import app.generated.projectT.model.ProductEditRequest;
import app.generated.projectT.tables.pojos.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface ProductEditMapper {
    ProductEditMapper MAPPER = Mappers.getMapper(ProductEditMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "nextId"),
            @Mapping(target = "createdDatetime", source = "now")
    })
    void map(@MappingTarget Product product, Long nextId, LocalDateTime now, ProductEditRequest productEditRequest);

    @Mapping(target = "productId", source = "newProductId")
    RefProductComponent updateId(RefProductComponent ref, Long newProductId);

    @Mapping(target = "productId", source = "newProductId")
    RefProductDiscount updateId(RefProductDiscount ref, Long newProductId);

    @Mapping(target = "productId", source = "newProductId")
    RefOrderProduct updateId(RefOrderProduct ref, Long newProductId);

    @Mapping(target = "productId", source = "newProductId")
    RefComboProduct updateId(RefComboProduct ref, Long newProductId);
}
