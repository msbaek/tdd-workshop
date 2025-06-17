package pe.msbaek.tdd_workshop.bowling;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/// - [x] gutter game (모든 투구에서 0점)
/// - [x] all ones (모든 프레임에서 핀을 한개씩만 쓰러뜨린 경우)
/// - [x] one spare (하나의 프레임만 스페어 처리하고, 다른 프레임은 open인 경우)
/// - [x] one strike (하나의 프레임만 스트라이크 처리하고, 다른 프레임은 open인 경우)
/// - [x] perfect game (모든 프레임에서 스트라이크)
/// - [x] complex case (스트라이크, 스페어, 오픈 프레임이 섞인 복합 케이스)
class BowlingGameTest {

    @Test
    @DisplayName("gutter game (모든 투구에서 0점)")
    void gutter_game() {
        assertScore(0)
            .when()
            .rollMany(20, 0);
    }
    
    @Test
    @DisplayName("all ones (모든 프레임에서 핀을 한개씩만 쓰러뜨린 경우)")
    void all_ones() {
        assertScore(20)
            .when()
            .rollMany(20, 1);
    }
    
    @Test
    @DisplayName("one spare (하나의 프레임만 스페어 처리하고, 다른 프레임은 open인 경우)")
    void one_spare() {
        assertScore(16)
            .when()
            .roll(5).roll(5) // 스페어
            .roll(3)         // 보너스 대상
            .rollMany(17, 0);
    }
    
    @Test
    @DisplayName("one strike (하나의 프레임만 스트라이크 처리하고, 다른 프레임은 open인 경우)")
    void one_strike() {
        assertScore(24)
            .when()
            .roll(10)        // 스트라이크
            .roll(3).roll(4) // 보너스 대상
            .rollMany(16, 0);
    }
    
    @Test
    @DisplayName("perfect game (모든 프레임에서 스트라이크)")
    void perfect_game() {
        assertScore(300)
            .when()
            .rollMany(12, 10); // 12번의 스트라이크
    }
    
    @Test
    @DisplayName("complex case (스트라이크, 스페어, 오픈 프레임이 섞인 복합 케이스)")
    void complex_case() {
        assertScore(168)
            .when()
            .roll(10)             // 1프레임: 스트라이크
            .roll(9).roll(0)      // 2프레임: 오픈 프레임
            .roll(10)             // 3프레임: 스트라이크
            .roll(2).roll(8)      // 4프레임: 스페어
            .roll(7).roll(0)      // 5프레임: 오픈 프레임
            .roll(10)             // 6프레임: 스트라이크
            .roll(10)             // 7프레임: 스트라이크
            .roll(9).roll(0)      // 8프레임: 오픈 프레임
            .roll(10)             // 9프레임: 스트라이크
            .roll(8).roll(2).roll(9); // 10프레임: 스페어 + 추가 투구
    }
    
    // DSL for fluent test writing
    private GameScenario assertScore(int expectedScore) {
        return new GameScenario(expectedScore);
    }
    
    private static class GameScenario {
        private final int expectedScore;
        private BowlingGame game;
        
        public GameScenario(int expectedScore) {
            this.expectedScore = expectedScore;
        }
        
        public GameBuilder when() {
            this.game = new BowlingGame();
            return new GameBuilder(this);
        }
        
        void verifyScore() {
            assertEquals(expectedScore, game.score());
        }
    }
    
    private static class GameBuilder {
        private final GameScenario scenario;
        
        public GameBuilder(GameScenario scenario) {
            this.scenario = scenario;
        }
        
        public GameBuilder roll(int pins) {
            scenario.game.roll(pins);
            return this;
        }
        
        public GameBuilder rollMany(int times, int pins) {
            for (int i = 0; i < times; i++) {
                roll(pins);
            }
            scenario.verifyScore();
            return this;
        }
    }
}
