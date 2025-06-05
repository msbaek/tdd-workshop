package pe.msbaek.tdd_workshop.shoppingbasket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.approvaltests.Approvals;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketDetailsResponse;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketItemRequest;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketItemRequests;
import pe.msbaek.tdd_workshop.shoppingbasket.CreateShoppingBasket.BasketResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateShoppingBasketTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
}