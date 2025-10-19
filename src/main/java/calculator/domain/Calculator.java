package calculator.domain;

import java.util.regex.Pattern;

public class Calculator {
    private static final String DEFAULT_DELIMITERS = ",|:";
    public int add(String input) {

        String numbersPart = input;
        String delimiters = DEFAULT_DELIMITERS;

        if(input.isEmpty()){
            return 0;
        }

        if (input.startsWith("//")) {
            int newlineIndex = input.indexOf("\\n");
            if (newlineIndex == -1) {
                throw new IllegalArgumentException("커스텀 구분자 형식 오류: `\\n`이 들어가야 합니다.");
            }

            String delimiterPart = input.substring(2, newlineIndex);

            if (delimiterPart.length() != 1) {
                throw new IllegalArgumentException("커스텀 구분자는 한 글자여야 합니다.");
            }
            char customDelimiter = delimiterPart.charAt(0);

            if (Character.isDigit(customDelimiter)) {
                throw new IllegalArgumentException("커스텀 구분자는 숫자일 수 없습니다.");
            }

            numbersPart = input.substring(newlineIndex + 2);
            delimiters = delimiters + "|" + Pattern.quote(String.valueOf(customDelimiter));
        }

        String[] numberTokens = numbersPart.split(delimiters);

        return sumTokens(numberTokens);
    }

    private int sumTokens(String[] tokens) {
        int sum = 0;
        for (String token : tokens) {
            try {
                int number = Integer.parseInt(token);

                if (number <= 0) {
                    throw new IllegalArgumentException("음수 또는 0은 포함될 수 없습니다.");
                }
                sum += number;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("숫자가 아닌 값이 포함되어 있습니다.");
            }
        }
        return sum;
    }
}
