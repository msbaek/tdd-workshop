package pe.msbaek.tdd_workshop.shoppingbasket;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Embeddable
@Accessors(fluent = true)
@Getter
public class BasketItem {
    @Column(name = "item_name")
    private String name;

    @Column(name = "item_price", precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "item_quantity")
    private int quantity;

    @Column(name = "item_total", precision = 19, scale = 2)
    private BigDecimal itemTotal;

    // JPA용 기본 생성자
    protected BasketItem() {}

    public BasketItem(String name, BigDecimal price, int quantity, BigDecimal itemTotal) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.itemTotal = itemTotal;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getItemTotal() {
        return itemTotal;
    }
}
