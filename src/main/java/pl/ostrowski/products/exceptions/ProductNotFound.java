package pl.ostrowski.products.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ProductNotFound extends RuntimeException {
    public ProductNotFound(long sku) {
        super(String.format("Product with id: %d not found", sku));
    }
}
