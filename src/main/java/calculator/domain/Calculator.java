package calculator.domain;

public class Calculator {
    private final Parser parser;

    public Calculator() {
        parser = new Parser();
    }

    public int add(String input) {
        if(input.isEmpty()){
            return 0;
        }
        String[] numberTokens = parser.parse(input);
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
