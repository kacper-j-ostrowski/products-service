package pl.ostrowski.products.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.ostrowski.products.datamodel.Product;
import pl.ostrowski.products.exceptions.ProductNotFound;
import pl.ostrowski.products.exceptions.SkuNotAvailable;
import pl.ostrowski.products.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTestOfValidations {

    private ProductService productService;

    private ProductRepository productRepository;

    @BeforeEach
    void setupRepository() {
        productRepository = mock(ProductRepository.class);

        Optional<Product> nonEmptyOptional = Optional.of(new Product());
        when(productRepository.findById(1L)).thenReturn(nonEmptyOptional);

        Optional<Product> emptyOptional = Optional.empty();
        when(productRepository.findById(2L)).thenReturn(emptyOptional);

        Optional<Product> obsoleteProduct = Optional.of(new Product(3L, "abc", BigDecimal.TEN, LocalDateTime.now(), true));
        when(productRepository.findById(3L)).thenReturn(obsoleteProduct);

        productService = new ProductService(productRepository);
    }

    @Test
    void whenUpdatingOfNonExistingProduct_ThrowException() {
        assertThrows(ProductNotFound.class, () -> {
            productService.updateProduct(new Product(2L, "abc", BigDecimal.ONE, LocalDateTime.now(), false));
        });
    }

    @Test
    void whenUpdatingOfObsoletedProduct_ThrowException() {
        assertThrows(ProductNotFound.class, () -> {
            productService.updateProduct(new Product(3L, "abc", BigDecimal.ONE, LocalDateTime.now(), false));
        });
    }

    @Test
    void whenSavingProductWithAlreadyExistingSku_ThrowException() {
        assertThrows(SkuNotAvailable.class, () -> {
            productService.saveNewProduct(new Product(1L, "abc", BigDecimal.ONE, LocalDateTime.now(), false));
        });
    }

}