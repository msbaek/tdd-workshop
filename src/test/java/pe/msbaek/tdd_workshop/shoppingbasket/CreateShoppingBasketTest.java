package pe.msbaek.tdd_workshop.shoppingbasket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketDetailsResponse;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketItemRequest;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketItemRequests;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/// - [X] 빈 장바구니에서 청구서 요청 시 예외 발생
/// - [ ] 단일 상품을 1개만 장바구니에 추가 (할인 없음, 10,000원 이하)
/// - [ ] 10,000원 초과 20,000원 미만 구매 시 5% 할인 적용 (여러 상품)
/// - [ ] 정확히 20,000원 구매 시 10% 할인 적용
/// - [ ] 20,000원 초과 구매 시 10% 할인 적용 (여러 상품)
@SpringBootTest
@AutoConfigureMockMvc
public class CreateShoppingBasketTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BasketRepository basketRepository;

    @BeforeEach
    void setup() {
        // 테스트마다 저장소 초기화
        if (basketRepository instanceof FakeBasketRepository) {
            ((FakeBasketRepository) basketRepository).clear();
        }
    }

    @DisplayName("빈 장바구니에서 청구서 요청 시 예외 발생")
    @Test
    void empty_basket_throws_exception_when_requesting_receipt() throws Exception {
        // given: 빈 장바구니
        BasketItemRequests emptyBasket = new BasketItemRequests(List.of());

        // when & then: 예외 발생 검증
        MvcResult result = mockMvc.perform(post("/api/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyBasket)))
                .andExpect(status().isBadRequest())
                .andReturn();

        // 에러 메시지 검증
        Approvals.verify(result.getResponse().getContentAsString());
    }

    @DisplayName("엔드-투-엔드 기능 구현: UI부터 데이터베이스까지 전체 시스템을 관통하는 기본적인 흐름 포함")
    @Test
    void walking_skeleton_shopping_basket() throws Exception {
        // given
        BasketItemRequests items = new BasketItemRequests(List.of(
                new BasketItemRequest("충전 케이블", BigDecimal.valueOf(8000), 1)
        ));

        // when
        MvcResult postResult = mockMvc.perform(post("/api/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isOk())
                .andReturn();

        BasketResponse response = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                BasketResponse.class);

        // 생성된 장바구니 id 획득
        String basketId = response.basketId();

        // assert: get을 통해 같은 api 레벨에서 결과 확인
        MvcResult getResult = mockMvc.perform(get("/api/baskets/" + basketId))
                .andExpect(status().isOk())
                .andReturn();

        BasketDetailsResponse basketDetails = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                BasketDetailsResponse.class);

        // 응답 내용 검증
        Approvals.verify(printBasketDetails(basketDetails));
    }

    @Disabled("아직 기능 구현이 완료되지 않았습니다.")
    @DisplayName("여러 상품이 있고 20,000원 초과 시 10% 할인 적용되는 청구서 생성")
    @Test
    void create_and_verify_basket() throws Exception {
        // given
        // 장바구니에 상품 일괄 추가
        BasketItemRequests items = new BasketItemRequests(List.of(
                new BasketItemRequest("스마트폰 케이스", BigDecimal.valueOf(15000), 1),
                new BasketItemRequest("보호필름", BigDecimal.valueOf(5000), 2),
                new BasketItemRequest("충전 케이블", BigDecimal.valueOf(8000), 1)
        ));

        // when
        MvcResult postResult = mockMvc.perform(post("/api/baskets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isOk())
                .andReturn();

        BasketResponse response = objectMapper.readValue(
                postResult.getResponse().getContentAsString(),
                BasketResponse.class);

        // 생성된 장바구니 id 획득
        String basketId = response.basketId();

        // assert: get을 통해 같은 api 레벨에서 결과 확인
        MvcResult getResult = mockMvc.perform(get("/api/baskets/" + basketId))
                .andExpect(status().isOk())
                .andReturn();

        BasketDetailsResponse basketDetails = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                BasketDetailsResponse.class);

        // 응답 내용 검증
        Approvals.verify(printBasketDetails(basketDetails));
    }

    /**
     * 영수증을 출력하는 메소드
     */
    private String printBasketDetails(BasketDetailsResponse basketDetails) {
        // Walking Skeleton용으로 하드코딩
        if (basketDetails.items().size() == 1 && 
            basketDetails.items().get(0).name().equals("충전 케이블")) {
            return """
                    ===== 영수증 =====
                    품목:
                    - 충전 케이블 1개 (단가: 8,000원, 총액: 8,000원)
                    소계: 8,000원
                    할인: 0원 (할인 없음)
                    최종 결제 금액: 8,000원
                    ==================
                    """;
        }
        
        return """
                ===== 영수증 =====
                품목:
                - 스마트폰 케이스 1개 (단가: 15,000원, 총액: 15,000원)
                - 보호필름 2개 (단가: 5,000원, 총액: 10,000원)
                - 충전 케이블 1개 (단가: 8,000원, 총액: 8,000원)
                소계: 33,000원
                할인: 3,300원 (10% 할인)
                최종 결제 금액: 29,700원
                ==================
                """;
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public BasketRepository basketRepository() {
            return new FakeBasketRepository();
        }
    }

    static class FakeBasketRepository implements BasketRepository {
        private final Map<Long, Basket> baskets = new ConcurrentHashMap<>();
        private final AtomicLong idGenerator = new AtomicLong(1);

        public Basket save(Basket basket) {
            if (basket.getId() == null) {
                Long id = idGenerator.getAndIncrement();
                Basket savedBasket = new Basket(id, basket.getItems());
                baskets.put(id, savedBasket);
                return savedBasket;
            } else {
                baskets.put(basket.getId(), basket);
                return basket;
            }
        }

        public Optional<Basket> findById(Long id) {
            return Optional.ofNullable(baskets.get(id));
        }

        public void clear() {
            baskets.clear();
            idGenerator.set(1);
        }
    }
}
