package pe.msbaek.tdd_workshop.shopping;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepositoryJpa extends JpaRepository<Basket, Long> {
}
