package calculator;

import calculator.domain.Calculator;
import calculator.ui.InputView;
import calculator.ui.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        Calculator calculator = new Calculator();

        outputView.printPrompt();
        String input = inputView.readInput();

        int result = calculator.add(input);

        outputView.printResult(result);
    }
}
