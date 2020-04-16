package pl.ostrowski.products.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ostrowski.products.datamodel.Product;
import pl.ostrowski.products.exceptions.ProductNotFound;
import pl.ostrowski.products.exceptions.SkuNotAvailable;
import pl.ostrowski.products.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product saveNewProduct(Product newProduct) {
        productRepository.findById(newProduct.getSku())
                .ifPresent((p) -> {
                    throw new SkuNotAvailable(newProduct.getSku());
                });
        return productRepository.save(newProduct);
    }

    public void deleteProduct(long productSku) {
        Product toDelete = getOrThrowProductNotFound(productSku);
        if (!toDelete.isObsolete()) {
            toDelete.setObsolete(true);
            productRepository.save(toDelete);
        }
    }

    public List<Product> fetchAllProducts() {
        return productRepository.fetchAllActiveProducts();
    }

    public Product updateProduct(Product toUpdate) {
        Product product = getOrThrowProductNotFound(toUpdate.getSku());
        if (product.isObsolete()) {
            throw new ProductNotFound(toUpdate.getSku());
        }
        return productRepository.save(toUpdate);
    }

    private Product getOrThrowProductNotFound(long sku) {
        Optional<Product> product = productRepository.findById(sku);
        if (product.isPresent()) {
            return product.get();
        }
        throw new ProductNotFound(sku);
    }
}
