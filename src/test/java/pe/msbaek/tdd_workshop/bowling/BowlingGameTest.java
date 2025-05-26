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
        // Given
        BowlingGame game = new BowlingGame();
        
        // When
        for (int i = 0; i < 20; i++) {
            game.roll(0);
        }
        
        // Then
        assertEquals(0, game.score());
    }
    
    @Test
    @DisplayName("all ones (모든 프레임에서 핀을 한개씩만 쓰러뜨린 경우)")
    void all_ones() {
        // Given
        BowlingGame game = new BowlingGame();
        
        // When
        for (int i = 0; i < 20; i++) {
            game.roll(1);
        }
        
        // Then
        assertEquals(20, game.score());
    }
    
    @Test
    @DisplayName("one spare (하나의 프레임만 스페어 처리하고, 다른 프레임은 open인 경우)")
    void one_spare() {
        // Given
        BowlingGame game = new BowlingGame();
        
        // When
        game.roll(5);  // 첫 번째 프레임 첫 투구
        game.roll(5);  // 첫 번째 프레임 두 번째 투구 (스페어)
        game.roll(3);  // 두 번째 프레임 첫 투구 (스페어 보너스로 사용됨)
        
        // 나머지 투구들은 모두 0
        for (int i = 0; i < 17; i++) {
            game.roll(0);
        }
        
        // Then
        assertEquals(16, game.score()); // 첫 프레임: 10+3=13, 두 번째 프레임: 3, 총 16점
    }
    
    @Test
    @DisplayName("one strike (하나의 프레임만 스트라이크 처리하고, 다른 프레임은 open인 경우)")
    void one_strike() {
        // Given
        BowlingGame game = new BowlingGame();
        
        // When
        game.roll(10); // 첫 번째 프레임 스트라이크
        game.roll(3);  // 두 번째 프레임 첫 투구 (스트라이크 보너스로 사용됨)
        game.roll(4);  // 두 번째 프레임 두 번째 투구 (스트라이크 보너스로 사용됨)
        
        // 나머지 투구들은 모두 0
        for (int i = 0; i < 16; i++) {
            game.roll(0);
        }
        
        // Then
        assertEquals(24, game.score()); // 첫 프레임: 10+3+4=17, 두 번째 프레임: 7, 총 24점
    }
    
    @Test
    @DisplayName("perfect game (모든 프레임에서 스트라이크)")
    void perfect_game() {
        // Given
        BowlingGame game = new BowlingGame();
        
        // When
        for (int i = 0; i < 12; i++) { // 10번째 프레임 스트라이크 시 추가 2번의 투구 포함
            game.roll(10);
        }
        
        // Then
        assertEquals(300, game.score()); // 퍼펙트 게임: 총 300점
    }
    
    @Test
    @DisplayName("complex case (스트라이크, 스페어, 오픈 프레임이 섞인 복합 케이스)")
    void complex_case() {
        // Given
        BowlingGame game = new BowlingGame();
        
        // When
        // X, 9/0, X, 2/8, 7/0, X, X, 9/0, X, 8/2/9
        game.roll(10);    // 1프레임: 스트라이크
        game.roll(9);     // 2프레임: 9
        game.roll(0);     // 2프레임: 0 (오픈 프레임)
        game.roll(10);    // 3프레임: 스트라이크
        game.roll(2);     // 4프레임: 2
        game.roll(8);     // 4프레임: 8 (스페어)
        game.roll(7);     // 5프레임: 7
        game.roll(0);     // 5프레임: 0 (오픈 프레임)
        game.roll(10);    // 6프레임: 스트라이크
        game.roll(10);    // 7프레임: 스트라이크
        game.roll(9);     // 8프레임: 9
        game.roll(0);     // 8프레임: 0 (오픈 프레임)
        game.roll(10);    // 9프레임: 스트라이크
        game.roll(8);     // 10프레임: 8
        game.roll(2);     // 10프레임: 2 (스페어)
        game.roll(9);     // 10프레임 추가 투구: 9
        
        // Then
        assertEquals(168, game.score()); // 총 168점
    }
}
