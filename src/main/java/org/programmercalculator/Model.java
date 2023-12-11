package org.programmercalculator;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final List<Integer> numbers = new ArrayList<>();
    private final List<String> operators = new ArrayList<>();

    public void addNumberToList(Integer num) {
        numbers.add(num);
    }

    public void addOperatorToList(String op) {
        operators.add(op);
    }

    public String removeLastOperatorFromList() {
        return operators.remove(operators.size() - 1);
    }

    public List<Integer> getNumbers() {
        return new ArrayList<>(numbers);
    }

    public List<String> getOperators() {
        return new ArrayList<>(operators);
    }

    public void clear() {
        numbers.clear();
        operators.clear();
    }

    public int getResult() { // This method evaluates a mathematical expression according to Java operator precedence
        negate();
        multiplyAndDivide();
        addAndSubtract();
        shiftLeftAndShiftRight();
        performBinaryBitwiseOperations();
        return numbers.get(0);
    }

    private void negate() {
        while (operators.contains("¬")) {
            int index = operators.indexOf("¬");
            Integer numberToNegate = numbers.remove(index);
            numbers.add(index, ~numberToNegate);
            operators.remove(index);
        }
    }

    private void multiplyAndDivide() {
        performInverseOperations("*", "/");
    }

    private void addAndSubtract() {
        performInverseOperations("+", "-");
    }

    private void shiftLeftAndShiftRight() {
        performInverseOperations("shl", "shr");
    }

    private void performInverseOperations(String op1, String op2){
        while (operators.contains(op1) || operators.contains(op2)) {
            int op1Index = operators.indexOf(op1);
            int op2Index = operators.indexOf(op2);
            if (op1Index != -1 && op2Index != -1) {
                executeBinaryOperation(Integer.min(op1Index, op2Index));
            } else {
                executeBinaryOperation(Integer.max(op1Index, op2Index));
            }
        }
    }

    private void performBinaryBitwiseOperations() {
        while (operators.contains("and")) {
            executeBinaryOperation(operators.indexOf("and"));
        }
        while (operators.contains("xor")) {
            executeBinaryOperation(operators.indexOf("xor"));
        }
        while (operators.contains("or")) {
            executeBinaryOperation(operators.indexOf("or"));
        }
    }


    private void executeBinaryOperation(int index) {
        int temp = 0;
        String operator = operators.get(index);
        switch (operator) {
            case "*":
                temp = numbers.get(index) * numbers.get(index + 1);
                break;
            case "/":
                temp = numbers.get(index) / numbers.get(index + 1);
                break;
            case "-":
                temp = numbers.get(index) - numbers.get(index + 1);
                break;
            case "+":
                temp = numbers.get(index) + numbers.get(index + 1);
                break;
            case "shl":
                temp = numbers.get(index) << numbers.get(index + 1);
                break;
            case "shr":
                temp = numbers.get(index) >> numbers.get(index + 1);
                break;
            case "and":
                temp = numbers.get(index) & numbers.get(index + 1);
                break;
            case "xor":
                temp = numbers.get(index) ^ numbers.get(index + 1);
                break;
            case "or":
                temp = numbers.get(index) | numbers.get(index + 1);
                break;
        }
        numbers.remove(index);
        numbers.remove(index);
        numbers.add(index, temp);
        operators.remove(index);
    }
}
