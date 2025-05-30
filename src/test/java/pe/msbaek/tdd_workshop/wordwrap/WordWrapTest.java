package pe.msbaek.tdd_workshop.wordwrap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

/// - [X] 빈 문자열 처리
/// - [X] null 문자열 처리  
/// - [X] width보다 짧은 단일 단어
/// - [X] width와 같은 길이의 단일 단어
/// - [X] 줄바꿈이 필요 없는 여러 단어
/// - [X] 공백에서 줄바꿈이 필요한 경우
/// - [X] width보다 긴 단어 강제 분할
/// - [X] 연속된 공백 처리
/// - [ ] 여러 줄에 걸친 복잡한 텍스트
class WordWrapTest {

    @DisplayName("빈 문자열 처리")
    @Test
    void wordWrap_should_pass() {
        assertWraps("", 5, "");
//        String result = WordWrap.wrap(text, width);
//
//        // then
//        assertThat(result).isEmpty();
    }

    @DisplayName("null 문자열 처리")
    @Test
    void null_string_returns_empty_string() {
        assertWraps(null, 5, "");
//        String result = WordWrap.wrap(text, width);
//
//        // then
//        assertThat(result).isEmpty();
    }

    @DisplayName("width보다 짧은 단일 단어")
    @Test
    void single_word_shorter_than_width() {
        assertWraps("word", 10, "word");
    }

    @Disabled("실패하지 않는 테스트")
    @DisplayName("width와 같은 길이의 단일 단어")
    @Test
    void single_word_same_length_as_width() {
        assertWraps("hello", 5, "hello");
    }

    @Disabled("실패하지 않는 테스트")
    @DisplayName("줄바꿈이 필요 없는 여러 단어")
    @Test
    void multiple_words_no_wrapping_needed() {
        assertWraps("hello world", 15, "hello world");
    }

    @DisplayName("공백에서 줄바꿈이 필요한 경우")
    @Test
    void wrapping_at_space_when_needed() {
        assertWraps("hello world", 7, "hello\nworld");
    }

    @DisplayName("width보다 긴 단어 강제 분할")
    @Test
    void force_wrap_long_word() {
        assertWraps("programming", 5, "progr\nammin\ng");
    }

    @DisplayName("연속된 공백 처리")
    @Test
    void handle_multiple_spaces() {
        assertWraps("hello    world", 12, "hello   \nworld");
    }

    private void assertWraps(final String text, final int width, final String expected) {
        // when
        String result = WordWrap.wrap(text, width);

        // then
        assertThat(result).isEqualTo(expected);
    }
}