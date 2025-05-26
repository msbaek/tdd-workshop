package pe.msbaek.tdd_workshop.bowling;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/// - [ ] gutter game (모든 투구에서 0점)
/// - [ ] all ones (모든 프레임에서 핀을 한개씩만 쓰러뜨린 경우)
/// - [ ] one spare (하나의 프레임만 스페어 처리하고, 다른 프레임은 open인 경우)
/// - [ ] one strike (하나의 프레임만 스트라이크 처리하고, 다른 프레임은 open인 경우)
/// - [ ] perfect game (모든 프레임에서 스트라이크)
/// - [ ] complex case (스트라이크, 스페어, 오픈 프레임이 섞인 복합 케이스)
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
}
