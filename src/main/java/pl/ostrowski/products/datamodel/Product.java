package pl.ostrowski.products.datamodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    long sku;

    @Length(min = 1, max = 100)
    @NotNull
    String name;

    @DecimalMin(value = "0.0")
    @Digits(integer = 10, fraction = 2)
    @NotNull
    BigDecimal price;

    @Column(updatable = false)
    private LocalDateTime createDateTime;

    boolean obsolete;

    @PrePersist
    public void addTimestamp() {
        createDateTime = LocalDateTime.now();
    }
}
