package pl.ostrowski.products.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import pl.ostrowski.products.datamodel.Product;
import pl.ostrowski.products.dto.ProductDto;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product fromDto(ProductDto productDto);

    @InheritInverseConfiguration
    ProductDto fromEntity(Product product);
}
