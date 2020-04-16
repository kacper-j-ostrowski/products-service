package pl.ostrowski.products.exceptions;

public class SkuNotAvailable extends RuntimeException {

    long invalidSku;

    public SkuNotAvailable(long sku) {
        this.invalidSku = sku;
    }

    public long getInvalidSku() {
        return invalidSku;
    }
}
