package com.example.ia_fxgui.services;

import com.example.ia_fxgui.Main;

import java.util.Stack;

class TreeNode {
    String value;
    TreeNode left, right;

    public TreeNode(String value) {
        this.value = value;
        left = right = null;
    }
}

public class EquationReader {

    public static Double evaluate(String expression, Double x) {
        TreeNode root = buildExpressionTree(expression);
        return evaluate(root, x);
    }

    public static Double evaluate(TreeNode root, Double x) {
        if (root == null) {
            throw new IllegalArgumentException("Invalid expression tree!");
        }

        if (!isOperator(root.value.charAt(0))) {
            if (!root.value.equals("x")) {
                Main.WarningPopup.openPopup("Variable must be 'x'");
                throw new IllegalArgumentException("Variable must be 'x'");
            }
            return x;
        }

        Double leftValue = evaluate(root.left, x);
        Double rightValue = evaluate(root.right, x);

        if (root.value.equals("^")) {
            return Math.pow(leftValue, rightValue);
        }

        return performOperation(root.value.charAt(0), leftValue, rightValue);
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static Double performOperation(char operator, Double operand1, Double operand2) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    Main.WarningPopup.openPopup("Division by zero!");
                    throw new ArithmeticException("Division by zero!");
                }
                return operand1 / operand2;
            default:
                Main.WarningPopup.openPopup("Invalid operator: " + operator);
                throw new IllegalArgumentException("Invalid operator: " + operator);

        }
    }

    private static TreeNode buildExpressionTree(String expression) {
        Stack<TreeNode> stack = new Stack<>();

        for (char c : expression.toCharArray()) {
            if (isOperator(c)) {
                TreeNode right = stack.pop();
                TreeNode left = stack.pop();
                TreeNode newNode = new TreeNode(Character.toString(c));
                newNode.left = left;
                newNode.right = right;
                stack.push(newNode);
            } else if (Character.isDigit(c) || c == 'x') {
                stack.push(new TreeNode(Character.toString(c)));
            }
        }
        return stack.pop();
    }
}
