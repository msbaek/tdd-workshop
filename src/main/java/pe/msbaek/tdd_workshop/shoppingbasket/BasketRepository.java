package pe.msbaek.tdd_workshop.shoppingbasket;

import java.util.Optional;

public interface BasketRepository {
    Basket save(Basket basket);
    Optional<Basket> findById(Long id);
}
