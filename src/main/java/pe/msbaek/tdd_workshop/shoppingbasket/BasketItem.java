package pe.msbaek.tdd_workshop.shoppingbasket;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Accessors(fluent = true)
@Getter
public class BasketItem {
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal itemTotal;

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
