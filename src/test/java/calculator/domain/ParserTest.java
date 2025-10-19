package calculator.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Parser 클래스 테스트")
class ParserTest {

    private Parser parser;

    @BeforeEach
    void setUp() {
        parser = new Parser();
    }

    // --- 기본 구분자 테스트 ---

    @Test
    @DisplayName("기본 구분자(쉼표)로 문자열을 분리한다")
    void parseWithCommaDelimiter() {
        // given
        String input = "1,2,3";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1", "2", "3");
    }

    @Test
    @DisplayName("기본 구분자(콜론)로 문자열을 분리한다")
    void parseWithColonDelimiter() {
        // given
        String input = "1:2:3";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1", "2", "3");
    }

    @Test
    @DisplayName("기본 구분자(쉼표, 콜론)를 섞어 문자열을 분리한다")
    void parseWithMixedDefaultDelimiters() {
        // given
        String input = "1,2:3";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1", "2", "3");
    }

    @Test
    @DisplayName("숫자가 하나일 경우에도 올바르게 분리한다")
    void parseWithSingleNumber() {
        // given
        String input = "1";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1");
    }

    // --- 커스텀 구분자 테스트 ---

    @Test
    @DisplayName("커스텀 구분자(;)로 문자열을 분리한다")
    void parseWithCustomDelimiter() {
        // given
        String input = "//;\\n1;2;3";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1", "2", "3");
    }

    @Test
    @DisplayName("커스텀 구분자와 기본 구분자를 모두 사용하여 분리한다")
    void parseWithCustomAndDefaultDelimiters() {
        // given
        String input = "//;\\n1;2,3:4";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1", "2", "3", "4");
    }

    @Test
    @DisplayName("커스텀 구분자가 정규식 특수문자(+)여도 올바르게 분리한다")
    void parseWithRegexSpecialCharDelimiter_Plus() {
        // given
        String input = "//+\\n1+2+3";

        // when
        String[] result = parser.parse(input);

        // then
        assertThat(result).containsExactly("1", "2", "3");
    }

    // --- 예외 처리 테스트 ---

    @Nested
    @DisplayName("유효성 검사 예외 테스트")
    class ValidationExceptionTests {

        @Test
        @DisplayName("헤더 종결자(\\n)가 없으면 IllegalArgumentException을 던진다")
        void validateHeaderTerminator_ThrowsException() {
            // given
            String input = "//;1;2;3";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> parser.parse(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("커스텀 구분자 형식 오류: `\\n`이 들어가야 합니다.");
        }

        @Test
        @DisplayName("커스텀 구분자가 한 글자가 아니면 IllegalArgumentException을 던진다")
        void validateDelimiterLength_ThrowsException() {
            // given
            String input = "//!!\\n1!!2";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> parser.parse(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("커스텀 구분자는 한 글자여야 합니다.");
        }

        @Test
        @DisplayName("커스텀 구분자가 숫자이면 IllegalArgumentException을 던진다")
        void validateDelimiterIsNotDigit_ThrowsException() {
            // given
            String input = "//1\\n11213";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> parser.parse(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("커스텀 구분자는 숫자일 수 없습니다.");
        }
    }
}