package com.example.ia_fxgui.services;

import com.example.ia_fxgui.Main;

import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;

public class EquationReader {
    public static Double evaluate(String expression, double xValue) {
        String postfixExpression = infixToPostfix(expression);
        return evaluatePostfix(postfixExpression, xValue);
    }

    private static Double evaluatePostfix(String postfixExpression, double xValue) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfixExpression.split("\\s+");
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            char c = token.charAt(0);
            if (Character.isDigit(c)) {
                try{
                stack.push(Double.parseDouble(token));}
                catch(Exception e){
                    Main.WarningPopup.openPopup("Spaces between expression parts needed");
                    e.printStackTrace();
                }
            } else if (c == 'x') {
                stack.push(xValue);
            } else if (isOperator(c)) {
                double operand2 = stack.pop();
                if (stack.isEmpty()) {
                    Main.WarningPopup.openPopup("Invalid expression format");
                    throw new IllegalArgumentException("Invalid expression format: insufficient operands");
                }
                double operand1 = stack.pop();
                stack.push(applyOperator(operand1, operand2, c));
            } else if (c == '~') {
                if (stack.isEmpty()) {
                    Main.WarningPopup.openPopup("Invalid expression format");
                    throw new IllegalArgumentException("Invalid expression format: insufficient operands for unary minus");
                }
                double operand = stack.pop();
                stack.push(-operand);
            }
        }
        if (stack.size() != 1) {
            Main.WarningPopup.openPopup("Invalid expression format");
            throw new IllegalArgumentException("Invalid expression format: incorrect number of operands");
        }
        return stack.pop();
    }

    private static String infixToPostfix(String expression) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> operatorStack = new Stack<>();
        String[] tokens = expression.split("\\s+");
        boolean expectOperand = true;
        for (String token : tokens) {
            if (token.isEmpty()) continue;
            char c = token.charAt(0);
            if (Character.isDigit(c)) {
                postfix.append(token).append(" ");
                expectOperand = false;
            } else if (c == 'x') {
                postfix.append(token).append(" ");
                expectOperand = false;
            } else if (c == '(') {
                operatorStack.push(c);
                expectOperand = true;
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                operatorStack.pop();
                expectOperand = false;
            } else if (isOperator(c)) {
                if (expectOperand && c == '-') {
                    // Unary minus '~' instead of '-'
                    c = '~';
                }
                while (!operatorStack.isEmpty() && precedence(operatorStack.peek()) >= precedence(c)) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                operatorStack.push(c);
                expectOperand = true;
            }
        }
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop()).append(" ");
        }
        return postfix.toString().trim();
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        } else if (operator == '^') {
            return 3;
        }
        return 0;
    }

    private static double applyOperator(double operand1, double operand2, char operator) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                return operand1 / operand2;
            case '^':
                return Math.pow(operand1, operand2);
            default:
                Main.WarningPopup.openPopup("Invalid operator");
                throw new IllegalArgumentException("Invalid operator");
        }
    }
}
