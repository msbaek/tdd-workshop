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
        
        // 여러 상품 처리
        List<BasketItem> items = requests.items().stream()
                .map(request -> new BasketItem(
                        request.name(), 
                        request.price(), 
                        request.quantity(),
                        request.price().multiply(BigDecimal.valueOf(request.quantity()))
                ))
                .toList();
        
        Basket basket = new Basket(null, items);
        Basket savedBasket = basketRepository.save(basket);
        
        return new BasketResponse(savedBasket.getId().toString());
    }

    @GetMapping("/{basketId}")
    public BasketDetailsResponse getBasketDetails(@PathVariable String basketId) {
        // Repository에서 데이터 읽어오기
        Long id = Long.valueOf(basketId);
        Basket basket = basketRepository.findById(id).get();
        
        // 소계 계산
        BigDecimal subtotal = basket.getItems().stream()
                .map(BasketItem::getItemTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 할인 계산 (TPP 준수 - 최소한의 구현)
        BigDecimal discount = BigDecimal.ZERO;
        if (subtotal.compareTo(BigDecimal.valueOf(10000)) > 0) {
            discount = subtotal.multiply(BigDecimal.valueOf(0.05));
        }

        BigDecimal total = subtotal.subtract(discount);

        // 도메인 모델을 DTO로 변환
        List<BasketItemDto> itemDtos = basket.getItems().stream()
                .map(item -> new BasketItemDto(item.getName(), item.getPrice(), item.getQuantity(), item.getItemTotal()))
                .toList();

        return new BasketDetailsResponse(
                basketId,
                itemDtos,
                subtotal,
                discount,
                total
        );
    }

    // Request/Response DTOs
    record BasketItemRequests(List<BasketItemRequest> items) {}
    record BasketItemRequest(String name, BigDecimal price, int quantity) {}
    record BasketResponse(String basketId) {}
    record BasketDetailsResponse(String basketId, List<BasketItemDto> items,
                                 BigDecimal subtotal, BigDecimal discount, BigDecimal total) {}
    record BasketItemDto(String name, BigDecimal price, int quantity, BigDecimal itemTotal) {}
}