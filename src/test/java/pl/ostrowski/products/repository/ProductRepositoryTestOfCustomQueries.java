package pl.ostrowski.products.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pl.ostrowski.products.datamodel.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ProductRepositoryTestOfCustomQueries {

    @Autowired
    private ProductRepository productRepository;


    @BeforeEach
    void prepareDataSet() {
        productRepository.save(new Product(1L, "Product-1", BigDecimal.ONE, LocalDateTime.now(), false));
        productRepository.save(new Product(2L, "Product-2", BigDecimal.ONE, LocalDateTime.now(), false));
        productRepository.save(new Product(3L, "Product-3", BigDecimal.ONE, LocalDateTime.now(), true));
        productRepository.save(new Product(4L, "Product-4", BigDecimal.ONE, LocalDateTime.now(), false));
        productRepository.save(new Product(5L, "Product-5", BigDecimal.ONE, LocalDateTime.now(), false));
        productRepository.save(new Product(6L, "Product-6", BigDecimal.ONE, LocalDateTime.now(), false));
    }

    @AfterEach
    void purgeDB() {
        productRepository.deleteAll();
    }

    @Test
    void whenQueriedAllProducts_ReturnAllNotObsoletedProducts() {
        // when
        List<Product> activeProducts = productRepository.fetchAllActiveProducts();

        // then we don't expect product with sku == 3, because it is softly deleted
        assertEquals(5, activeProducts.size());
        long productsWithSkuEqualsToThree = activeProducts
                .stream()
                .filter(p -> p.getSku() == 3L)
                .count();
        assertEquals(0, productsWithSkuEqualsToThree);
    }

}