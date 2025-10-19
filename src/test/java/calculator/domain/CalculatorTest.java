package calculator.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Calculator 클래스 테스트")
class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    // --- 핵심 기능 테스트 ---

    @Test
    @DisplayName("빈 문자열을 입력하면 0을 반환한다")
    void add_WithEmptyString_ShouldReturnZero() {
        // given
        String input = "";

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("숫자 하나를 입력하면 해당 숫자를 반환한다")
    void add_WithSingleNumber_ShouldReturnNumber() {
        // given
        String input = "5";

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(5);
    }

    @Test
    @DisplayName("기본 구분자(쉼표, 콜론)로 분리된 숫자들의 합을 반환한다")
    void add_WithDefaultDelimiters_ShouldReturnSum() {
        // given
        String input = "1,2:3";

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(6);
    }

    @Test
    @DisplayName("커스텀 구분자로 분리된 숫자들의 합을 반환한다")
    void add_WithCustomDelimiter_ShouldReturnSum() {
        // given
        String input = "//;\\n1;2;3";

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(6);
    }

    @Test
    @DisplayName("커스텀 구분자와 기본 구분자가 섞인 숫자들의 합을 반환한다")
    void add_WithMixedDelimiters_ShouldReturnSum() {
        // given
        String input = "//*\\n1*2,3:4";

        // when
        int result = calculator.add(input);

        // then
        assertThat(result).isEqualTo(10);
    }


    // --- sumTokens 예외 처리 테스트 ---

    @Nested
    @DisplayName("sumTokens 유효성 검사 예외 테스트")
    class SumTokensValidationTests {

        @Test
        @DisplayName("숫자가 아닌 값(a)이 포함되면 IllegalArgumentException을 던진다")
        void sumTokens_WithNonNumericValue_ShouldThrowException() {
            // given
            String input = "1,a,2";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("숫자가 아닌 값이 포함되어 있습니다.");
        }

        @Test
        @DisplayName("숫자 사이에 빈 값(,,)이 포함되면 NumberFormatException으로 인해 IllegalArgumentException을 던진다")
        void sumTokens_WithEmptyToken_ShouldThrowException() {
            // given
            String input = "1,,2";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("숫자가 아닌 값이 포함되어 있습니다.");
        }

        @Test
        @DisplayName("0이 포함되면 IllegalArgumentException을 던진다")
        void sumTokens_WithZero_ShouldThrowException() {
            // given
            String input = "1,0,2";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("음수 또는 0은 포함될 수 없습니다.");
        }

        @Test
        @DisplayName("음수가 포함되면 IllegalArgumentException을 던진다")
        void sumTokens_WithNegativeNumber_ShouldThrowException() {
            // given
            String input = "1,-5,2";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(input)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("음수 또는 0은 포함될 수 없습니다.");
        }
    }

    // --- Parser 예외 전파 테스트 ---

    @Nested
    @DisplayName("Parser 예외 전파 테스트")
    class ParserExceptionPropagationTests {

        @Test
        @DisplayName("Parser에서 구분자 형식 오류가 발생하면 예외가 그대로 전파된다")
        void add_WhenParserThrowsException_ShouldPropagate() {
            // given
            String badParserInput = "//1\\n112";

            // when & then
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> calculator.add(badParserInput)
            );

            // then
            assertThat(exception.getMessage()).isEqualTo("커스텀 구분자는 숫자일 수 없습니다.");
        }
    }
}