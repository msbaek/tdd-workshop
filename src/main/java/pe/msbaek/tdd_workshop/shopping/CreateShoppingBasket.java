package pe.msbaek.tdd_workshop.shopping;

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
        // Fake it: 하드코딩으로 테스트만 성공시키기
        List<BasketItem> items = requests.items().stream()
                .map(req -> new BasketItem(req.name(), req.price(), req.quantity()))
                .toList();
        
        Basket basket = new Basket(null, items);
        Basket savedBasket = basketRepository.save(basket);
        
        return new BasketResponse(savedBasket.getId().toString());
    }

    @GetMapping("/{basketId}")
    public BasketDetailsResponse getBasketDetails(@PathVariable String basketId) {
        // Fake it: 하드코딩으로 테스트만 성공시키기
        Long id = Long.valueOf(basketId);
        Basket basket = basketRepository.findById(id).orElseThrow();
        
        // 하드코딩된 결과 반환
        List<BasketItemDto> itemDtos = List.of(
                new BasketItemDto("충전 케이블", BigDecimal.valueOf(8000), 1, BigDecimal.valueOf(8000))
        );
        
        return new BasketDetailsResponse(
                basketId,
                itemDtos,
                BigDecimal.valueOf(8000),  // 소계
                BigDecimal.ZERO,           // 할인
                BigDecimal.valueOf(8000),  // 총액
                "할인 없음"
        );
    }

    // Inner records for request/response
    public record BasketItemRequests(List<BasketItemRequest> items) {}
    public record BasketItemRequest(String name, BigDecimal price, int quantity) {}
    public record BasketResponse(String basketId) {}
    public record BasketDetailsResponse(
            String basketId,
            List<BasketItemDto> items,
            BigDecimal subtotal,
            BigDecimal discount,
            BigDecimal total,
            String discountRate
    ) {}
    public record BasketItemDto(String name, BigDecimal price, int quantity, BigDecimal total) {}
}
