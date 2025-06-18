package pe.msbaek.tdd_workshop.shopping;

import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class BasketRepositoryImpl implements BasketRepository {
    
    private final BasketRepositoryJpa basketRepositoryJpa;

    public BasketRepositoryImpl(BasketRepositoryJpa basketRepositoryJpa) {
        this.basketRepositoryJpa = basketRepositoryJpa;
    }

    @Override
    public Basket save(Basket basket) {
        return basketRepositoryJpa.save(basket);
    }

    @Override
    public Optional<Basket> findById(Long id) {
        return basketRepositoryJpa.findById(id);
    }
}
