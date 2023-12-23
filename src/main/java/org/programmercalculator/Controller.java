package org.programmercalculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    private final Model model = new Model();
    private final List<Button> buttons = new ArrayList<>();
    @FXML
    private Button num2;
    @FXML
    private Button num3;
    @FXML
    private Button num4;
    @FXML
    private Button num5;
    @FXML
    private Button num6;
    @FXML
    private Button num7;
    @FXML
    private Button num8;
    @FXML
    private Button num9;
    @FXML
    private Button A;
    @FXML
    private Button B;
    @FXML
    private Button D;
    @FXML
    private Button C;
    @FXML
    private Button E;
    @FXML
    private Button F;
    @FXML
    private Label screen;
    private String currentNumber = "";
    private String currentNumeralSystem = "dec";
    private String previousNumeralSystem = "dec";
    private boolean isCurrentNumberNegative;
    private boolean doNegateCurrentNumber;
    private boolean isBinaryOperatorChosen;
    private boolean start = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons.add(num2);
        buttons.add(num3);
        buttons.add(num4);
        buttons.add(num5);
        buttons.add(num6);
        buttons.add(num7);
        buttons.add(num8);
        buttons.add(num9);
        buttons.add(A);
        buttons.add(B);
        buttons.add(C);
        buttons.add(D);
        buttons.add(E);
        buttons.add(F);
        setCurrentNumeralSystemButtons();
    }

    @FXML
    private void processNumbers(ActionEvent event) {
        if (start) {
            screen.setText("");
            start = false;
        }
        String value = ((Button) event.getSource()).getText();
        if (currentNumber.equals("0")) {
            if (value.equals("0")) {
                return;
            }
            currentNumber = value;
            screen.setText(getCurrentTextWithoutLastValue("0") + value);
        } else {
            currentNumber += value;
            screen.setText(screen.getText() + value);
        }
        isBinaryOperatorChosen = false;
    }

    @FXML
    private void processBinaryOperators(ActionEvent event) {
        if (currentNumber.isBlank() && !isBinaryOperatorChosen) {
            return;
        }
        String value = ((Button) event.getSource()).getText();
        if (isBinaryOperatorChosen) {
            String prevOp = model.removeLastOperatorFromList();
            String textWithoutPreviousOperator = getCurrentTextWithoutLastValue(prevOp);
            screen.setText(textWithoutPreviousOperator + value);
        } else {
            model.addNumberToList(getCurrentNumberInDecimal(currentNumeralSystem));
            doNegateCurrentNumber = false;
            isCurrentNumberNegative = false;
            currentNumber = "";
            screen.setText(screen.getText() + value);
        }
        isBinaryOperatorChosen = true;
        model.addOperatorToList(value);
    }

    @FXML
    private void processUnaryOperators(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        if (currentNumber.isBlank()) {
            return;
        }
        String textWithoutCurrentNumber = getCurrentTextWithoutLastValue(currentNumber);
        if (value.equals("not")) {
            if (!doNegateCurrentNumber) {
                doNegateCurrentNumber = true;
                model.addOperatorToList("¬");
                textWithoutCurrentNumber += "¬";
            } else {
                doNegateCurrentNumber = false;
                model.removeLastOperatorFromList();
                textWithoutCurrentNumber = textWithoutCurrentNumber.substring(0, textWithoutCurrentNumber.length() - 1);
            }
        } else { //when "+/-" button is pressed
            if (currentNumber.equals("0")) {
                return;
            }
            if (!isCurrentNumberNegative) {
                isCurrentNumberNegative = true;
                currentNumber = "-" + currentNumber;
            } else {
                isCurrentNumberNegative = false;
                currentNumber = currentNumber.substring(1);
            }
        }
        screen.setText(textWithoutCurrentNumber + currentNumber);
    }

    @FXML
    private void processEqualsAndClear(ActionEvent event) {
        String value = ((Button) event.getSource()).getText();
        if (currentNumber.isBlank() && value.equals("=")) {
            return;
        }
        if (value.equals("=")) {
            model.addNumberToList(getCurrentNumberInDecimal(currentNumeralSystem));
            screen.setText(String.valueOf(convertNumberToCurrentNumeralSystem(model.getResult())));

        } else {
            screen.setText("");
        }
        currentNumber = "";
        model.clear();
        start = true;
        doNegateCurrentNumber = false;
        isCurrentNumberNegative = false;
        isBinaryOperatorChosen = false;
    }

    @FXML
    private void processNumeralSystems(ActionEvent event) {
        previousNumeralSystem = currentNumeralSystem;
        currentNumeralSystem = ((Button) event.getSource()).getText();
        setCurrentNumeralSystemButtons();
        convertInputToCurrentNumeralSystem();
    }

    private Integer getCurrentNumberInDecimal(String numeralSystem) {
        switch (numeralSystem) {
            case "hex":
                return Integer.parseInt(currentNumber, 16);
            case "dec":
                return Integer.parseInt(currentNumber);
            case "oct":
                return Integer.parseInt(currentNumber, 8);
            case "bin":
                return Integer.parseInt(currentNumber, 2);
            default:
                return 0;
        }
    }

    private String convertNumberToCurrentNumeralSystem(Integer num) {
        String temp = "";
        boolean isNegative = num < 0;
        switch (currentNumeralSystem) {
            case "hex":
                temp = Integer.toHexString(Math.abs(num)).toUpperCase();
                break;
            case "dec":
                temp = Integer.toString(Math.abs(num));
                break;
            case "oct":
                temp = Integer.toOctalString(Math.abs(num));
                break;
            case "bin":
                temp = Integer.toBinaryString(Math.abs(num));
                break;
        }
        return isNegative ? "-" + temp : temp;
    }

    private void convertInputToCurrentNumeralSystem() {
        List<Integer> numbers = model.getNumbers();
        List<String> operators = model.getOperators();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numbers.size(); ) {
            for (int j = 0; j < operators.size(); ) {
                if (operators.get(j).equals("¬")) {
                    result.append("¬");
                } else {
                    result.append(convertNumberToCurrentNumeralSystem(numbers.get(i++))).append(operators.get(j));
                }
                j++;
            }
        }
        if (!currentNumber.isBlank()) {
            currentNumber = convertNumberToCurrentNumeralSystem(getCurrentNumberInDecimal(previousNumeralSystem));
            result.append(currentNumber);
        }
        screen.setText(result.toString());
    }

    private String getCurrentTextWithoutLastValue(String value) {
        String currentText = screen.getText();
        return currentText.substring(0, currentText.length() - value.length());
    }

    private void setCurrentNumeralSystemButtons() {
        int buttonsToDisable = 0;
        switch (currentNumeralSystem) {
            case "hex":
                break;
            case "dec":
                buttonsToDisable = 6;
                break;
            case "oct":
                buttonsToDisable = 8;
                break;
            case "bin":
                buttonsToDisable = 14;
                break;
        }
        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).setDisable(i >= buttons.size() - buttonsToDisable);
        }
    }
}
