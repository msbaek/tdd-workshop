package pe.msbaek.tdd_workshop.wordwrap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/// - [X] 빈 문자열 처리
/// - [ ] null 문자열 처리  
/// - [ ] width보다 짧은 단일 단어
/// - [ ] width와 같은 길이의 단일 단어
/// - [ ] 줄바꿈이 필요 없는 여러 단어
/// - [ ] 공백에서 줄바꿈이 필요한 경우
/// - [ ] width보다 긴 단어 강제 분할
/// - [ ] 연속된 공백 처리
/// - [ ] 여러 줄에 걸친 복잡한 텍스트
class WordWrapTest {

    @DisplayName("빈 문자열 처리")
    @Test
    void empty_string_returns_empty_string() {
        // given
        String text = "";
        int width = 5;
        
        // when
        String result = WordWrap.wrap(text, width);
        
        // then
        assertThat(result).isEmpty();
    }
}