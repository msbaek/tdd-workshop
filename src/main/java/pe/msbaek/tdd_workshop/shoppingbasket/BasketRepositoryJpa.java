package pe.msbaek.tdd_workshop.shoppingbasket;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepositoryJpa extends JpaRepository<Basket, Long> {
}
