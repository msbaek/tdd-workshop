package pe.msbaek.tdd_workshop.shopping;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "baskets")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BasketItem> items;

    protected Basket() {
        // JPA 기본 생성자
    }

    public Basket(Long id, List<BasketItem> items) {
        this.id = id;
        this.items = items;
        // 양방향 관계 설정
        if (items != null) {
            items.forEach(item -> item.setBasket(this));
        }
    }

    public Long getId() {
        return id;
    }

    public List<BasketItem> getItems() {
        return items;
    }
}
