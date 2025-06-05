package pe.msbaek.tdd_workshop.shoppingbasket;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/baskets")
public class CreateShoppingBasket {

    private final BasketRepository basketRepository;

    public CreateShoppingBasket(BasketRepository basketRepository) {
        this.basketRepository = basketRepository;
    }

    @PostMapping
    public BasketResponse createBasket(@RequestBody BasketItemRequests requests) {
        // Fake it: 하드코딩으로 장바구니 생성
        List<BasketItem> items = List.of(
                new BasketItem("충전 케이블", BigDecimal.valueOf(8000), 1, BigDecimal.valueOf(8000))
        );
        
        Basket basket = new Basket(null, items);
        Basket savedBasket = basketRepository.save(basket);
        
        return new BasketResponse(savedBasket.getId().toString());
    }

    @GetMapping("/{basketId}")
    public BasketDetailsResponse getBasketDetails(@PathVariable String basketId) {
        // Fake it: 하드코딩으로 응답 생성
        List<BasketItem> responseItems = List.of(
                new BasketItem("충전 케이블", BigDecimal.valueOf(8000), 1, BigDecimal.valueOf(8000))
        );
        
        return new BasketDetailsResponse(
                basketId,
                responseItems,
                BigDecimal.valueOf(8000), // 소계
                BigDecimal.valueOf(0),    // 할인
                BigDecimal.valueOf(8000)  // 총액
        );
    }

    // Request/Response DTOs
    record BasketItemRequests(List<BasketItemRequest> items) {}
    record BasketItemRequest(String name, BigDecimal price, int quantity) {}
    record BasketResponse(String basketId) {}
    record BasketDetailsResponse(String basketId, List<BasketItem> items,
                                 BigDecimal subtotal, BigDecimal discount, BigDecimal total) {}
}
