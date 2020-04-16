package pl.ostrowski.products.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.ostrowski.products.datamodel.Product;
import pl.ostrowski.products.dto.ProductDto;
import pl.ostrowski.products.exceptions.ProductNotFound;
import pl.ostrowski.products.exceptions.SkuNotAvailable;
import pl.ostrowski.products.mapper.ProductMapper;
import pl.ostrowski.products.service.ProductService;

import javax.persistence.EntityNotFoundException;
import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @PostMapping(consumes = "application/json")
    ResponseEntity createProduct(@RequestBody ProductDto productDto) {
        Product newProduct = productMapper.fromDto(productDto);
        newProduct = productService.saveNewProduct(newProduct);
        log.info("Created new product with sku: {}", newProduct.getSku());
        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = "application/json")
    ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.fetchAllProducts()
                .stream()
                .map(productMapper::fromEntity)
                .collect(Collectors.toList()));
    }

    @PutMapping(consumes = "application/json")
    ResponseEntity updateProduct(@RequestBody ProductDto productDto) {
        Product toUpdate = productMapper.fromDto(productDto);
        toUpdate = productService.updateProduct(toUpdate);
        log.info("Updated product with sku: {}", toUpdate.getSku());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{productSku}")
    ResponseEntity deleteProduct(@PathVariable("productSku") long productSku) {
        log.info("Deleting product with sku number: {}", productSku);
        productService.deleteProduct(productSku);
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(value= HttpStatus.NOT_FOUND)
    @ExceptionHandler({EntityNotFoundException.class, ProductNotFound.class})
    public String handleNotFoundException(Exception e) {
        return "Not found entity with provided sku";
    }

    @ResponseStatus(value= HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SkuNotAvailable.class)
    public String handleSkuNotAvailable(SkuNotAvailable e) {
        log.warn("User chose reserved sku identifier: {}", e.getInvalidSku());
        return "Please choose other SKU identifier";
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleValidationException(Exception e) {
        if(e.getCause() instanceof RollbackException
                && e.getCause().getCause() instanceof ConstraintViolationException) {
            log.info("Provided wrong input data");
            return ResponseEntity.badRequest()
                    .body("Please correct name or price");
        }
        String errorId = UUID.randomUUID().toString();
        log.info("Unrecognized exception thrown with id {}", errorId, e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Something went wrong. Error id: " + errorId);
    }
}
