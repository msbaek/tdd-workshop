package pe.msbaek.tdd_workshop.shoppingbasket;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "basket_items", joinColumns = @JoinColumn(name = "basket_id"))
    private List<BasketItem> items;

    // JPA용 기본 생성자
    protected Basket() {}

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
