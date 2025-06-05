package pe.msbaek.tdd_workshop.shopping;

import java.util.List;

public class Basket {
    private Long id;
    private List<BasketItem> items;

    public Basket(Long id, List<BasketItem> items) {
        this.id = id;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public List<BasketItem> getItems() {
        return items;
    }
}
