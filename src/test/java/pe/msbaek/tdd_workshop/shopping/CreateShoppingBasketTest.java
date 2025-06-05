package pe.msbaek.tdd_workshop.shopping;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.approvaltests.Approvals;
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
import org.springframework.test.web.servlet.ResultMatcher;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static pe.msbaek.tdd_workshop.shopping.CreateShoppingBasket.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/// - [X] 빈 장바구니에서 청구서 요청 시 예외 발생
/// - [X] 단일 상품을 1개만 장바구니에 추가 (할인 없음, 10,000원 이하)
/// - [X] 10,000원 초과 20,000원 미만 구매 시 5% 할인 적용
/// - [X] 정확히 20,000원 구매 시 10% 할인 적용
@SpringBootTest
@AutoConfigureMockMvc
public class CreateShoppingBasketTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private BasketApi basketApi() {
        return new BasketApi(mockMvc, objectMapper);
    }

    @DisplayName("정확히 20,000원 구매 시 10% 할인 적용")
    @Test
    void apply_10_percent_discount_for_exactly_20000() throws Exception {
        // given & when
        String basketId = basketApi().createBasket(
                aBasket()
                        .withItem(anItem("스마트폰 케이스").withPrice(15000).withQuantity(1))
                        .withItem(anItem("보호필름").withPrice(5000).withQuantity(1))
        );

        // then
        verifyBasketReceipt(basketApi().getBasketDetails(basketId));
    }

    @DisplayName("10,000원 초과 20,000원 미만 구매 시 5% 할인 적용")
    @Test
    void apply_5_percent_discount_for_amount_between_10000_and_20000() throws Exception {
        // given & when
        String basketId = basketApi().createBasket(
                aBasket()
                        .withItem(anItem("스마트폰 케이스").withPrice(12000).withQuantity(1))
                        .withItem(anItem("보호필름").withPrice(3000).withQuantity(1))
        );

        // then
        verifyBasketReceipt(basketApi().getBasketDetails(basketId));
    }

    @DisplayName("단일 상품을 1개만 장바구니에 추가 (할인 없음, 10,000원 이하)")
    @Test
    void add_single_item_without_discount() throws Exception {
        // given & when
        String basketId = basketApi().createBasket(
                aBasket()
                        .withItem(anItem("충전 케이블").withPrice(8000).withQuantity(1))
        );

        // then
        verifyBasketReceipt(basketApi().getBasketDetails(basketId));
    }

    @DisplayName("빈 장바구니에서 청구서 요청 시 예외 발생")
    @Test
    void empty_basket_throws_exception_when_requesting_receipt() throws Exception {
        // given & when & then
        String errorMessage = basketApi().createBasketAndExpectError(aBasket());
        
        // 에러 메시지 검증
        Approvals.verify(errorMessage);
    }



    private void verifyBasketReceipt(BasketDetailsResponse basketDetails) {
        Approvals.verify(printBasketReceipt(basketDetails));
    }

    /**
     * 영수증을 출력하는 메소드
     */
    private String printBasketReceipt(BasketDetailsResponse basketDetails) {
        StringBuilder receipt = new StringBuilder();
        receipt.append("===== 영수증 =====\n");
        receipt.append("품목:\n");
        
        for (BasketItemDto item : basketDetails.items()) {
            receipt.append(String.format("- %s %d개 (단가: %s원, 총액: %s원)\n",
                    item.name(),
                    item.quantity(),
                    formatCurrency(item.price()),
                    formatCurrency(item.total())
            ));
        }
        
        receipt.append(String.format("소계: %s원\n", formatCurrency(basketDetails.subtotal())));
        receipt.append(String.format("할인: %s원 (%s)\n", 
                formatCurrency(basketDetails.discount()), basketDetails.discountRate()));
        receipt.append(String.format("최종 결제 금액: %s원\n", formatCurrency(basketDetails.total())));
        receipt.append("==================");
        
        return receipt.toString();
    }

    private String formatCurrency(BigDecimal amount) {
        return String.format("%,d", amount.intValue());
    }

    // Test Data Builders
    private static BasketBuilder aBasket() {
        return new BasketBuilder();
    }

    private static ItemBuilder anItem(String name) {
        return new ItemBuilder(name);
    }

    static class BasketBuilder {
        private final List<BasketItemRequest> items = new ArrayList<>();

        public BasketBuilder withItem(ItemBuilder itemBuilder) {
            items.add(itemBuilder.build());
            return this;
        }

        public BasketItemRequests build() {
            return new BasketItemRequests(items);
        }
    }

    static class ItemBuilder {
        private final String name;
        private BigDecimal price = BigDecimal.ZERO;
        private int quantity = 1;

        public ItemBuilder(String name) {
            this.name = name;
        }

        public ItemBuilder withPrice(int price) {
            this.price = BigDecimal.valueOf(price);
            return this;
        }

        public ItemBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public BasketItemRequest build() {
            return new BasketItemRequest(name, price, quantity);
        }
    }

    // Protocol Driver
    static class BasketApi {
        private final MockMvc mockMvc;
        private final ObjectMapper objectMapper;

        public BasketApi(MockMvc mockMvc, ObjectMapper objectMapper) {
            this.mockMvc = mockMvc;
            this.objectMapper = objectMapper;
        }

        public String createBasket(BasketBuilder basketBuilder) throws Exception {
            return createBasketWithStatus(basketBuilder, status().isOk());
        }

        public String createBasketAndExpectError(BasketBuilder basketBuilder) throws Exception {
            MvcResult result = mockMvc.perform(post("/api/baskets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(basketBuilder.build())))
                    .andExpect(status().isBadRequest())
                    .andReturn();

            return result.getResponse().getContentAsString();
        }

        private String createBasketWithStatus(BasketBuilder basketBuilder, ResultMatcher status) throws Exception {
            MvcResult postResult = mockMvc.perform(post("/api/baskets")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(basketBuilder.build())))
                    .andExpect(status)
                    .andReturn();

            BasketResponse response = objectMapper.readValue(
                    postResult.getResponse().getContentAsString(),
                    BasketResponse.class);

            return response.basketId();
        }

        public BasketDetailsResponse getBasketDetails(String basketId) throws Exception {
            MvcResult getResult = mockMvc.perform(get("/api/baskets/" + basketId))
                    .andExpect(status().isOk())
                    .andReturn();

            return objectMapper.readValue(
                    getResult.getResponse().getContentAsString(),
                    BasketDetailsResponse.class);
        }
    }

    // Fake Repository for Testing
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
