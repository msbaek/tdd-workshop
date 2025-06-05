package pe.msbaek.tdd_workshop.shoppingbasket;

import java.math.BigDecimal;
import java.util.List;

public class CreateShoppingBasket {

    // Request/Response DTOs
    record BasketItemRequests(List<BasketItemRequest> items) {}
    record BasketItemRequest(String name, BigDecimal price, int quantity) {}
    record BasketResponse(String basketId) {}
    record BasketDetailsResponse(String basketId, List<BasketItem> items,
                                 BigDecimal subtotal, BigDecimal discount, BigDecimal total) {}
    record BasketItem(String name, BigDecimal price, int quantity, BigDecimal itemTotal) {}
}
