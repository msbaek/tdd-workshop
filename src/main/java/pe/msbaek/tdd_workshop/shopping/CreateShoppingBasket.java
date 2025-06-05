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
        // 빈 장바구니 검증
        if (requests.items() == null || requests.items().isEmpty()) {
            throw new IllegalArgumentException("장바구니가 비어 있어서 영수증을 생성할 수 없습니다.");
        }
        
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
        Long id = Long.valueOf(basketId);
        Basket basket = basketRepository.findById(id).orElseThrow();
        
        // 실제 계산으로 변경
        List<BasketItemDto> itemDtos = basket.getItems().stream()
                .map(item -> new BasketItemDto(
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity(),
                        item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                ))
                .toList();
        
        // 소계 계산
        BigDecimal subtotal = itemDtos.stream()
                .map(BasketItemDto::total)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 할인 계산 (10,000원 이하는 할인 없음)
        BigDecimal discount = BigDecimal.ZERO;
        String discountRate = "할인 없음";
        
        // 최종 금액
        BigDecimal total = subtotal.subtract(discount);
        
        return new BasketDetailsResponse(
                basketId,
                itemDtos,
                subtotal,
                discount,
                total,
                discountRate
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
