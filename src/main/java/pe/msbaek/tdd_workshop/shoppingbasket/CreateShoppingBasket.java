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
        // 빈 장바구니 체크
        if (requests.items().isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어있어서 청구서를 생성할 수 없습니다.");
        }
        
        // Triangulation: 이제 두 개의 테스트가 있으므로 일반화
        BasketItemRequest firstItem = requests.items().get(0);
        List<BasketItem> items = List.of(
                new BasketItem(firstItem.name(), firstItem.price(), firstItem.quantity(), 
                              firstItem.price().multiply(BigDecimal.valueOf(firstItem.quantity())))
        );
        
        Basket basket = new Basket(null, items);
        Basket savedBasket = basketRepository.save(basket);
        
        return new BasketResponse(savedBasket.getId().toString());
    }

    @GetMapping("/{basketId}")
    public BasketDetailsResponse getBasketDetails(@PathVariable String basketId) {
        // Repository에서 데이터 읽어오기
        Long id = Long.valueOf(basketId);
        Basket basket = basketRepository.findById(id).get();
        final BasketItem basketItem = basket.getItems().get(0);

        return new BasketDetailsResponse(
                basketId,
                List.of(basketItem),
                basketItem.getItemTotal(), // 소계 (실제 계산된 값)
                BigDecimal.valueOf(0),     // 할인 (아직 Fake it)
                basketItem.getItemTotal()  // 총액 (할인 없으므로 소계와 동일)
        );
    }

    // Request/Response DTOs
    record BasketItemRequests(List<BasketItemRequest> items) {}
    record BasketItemRequest(String name, BigDecimal price, int quantity) {}
    record BasketResponse(String basketId) {}
    record BasketDetailsResponse(String basketId, List<BasketItem> items,
                                 BigDecimal subtotal, BigDecimal discount, BigDecimal total) {}
}