package app.mappers.product;

import app.generated.projectT.model.ProductCreateRequest;
import app.generated.projectT.tables.pojos.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper
public interface ProductCreateMapper {
    ProductCreateMapper MAPPER = Mappers.getMapper(ProductCreateMapper.class);

    @Mappings({
            @Mapping(target = "id", source = "nextId"),
            @Mapping(target = "entityId", source = "nextId"),
            @Mapping(target = "createdDatetime", source = "now"),
    })
    Product map(ProductCreateRequest productCreateRequest, Long nextId, LocalDateTime now);
}
