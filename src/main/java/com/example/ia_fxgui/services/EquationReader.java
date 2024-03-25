package com.example.ia_fxgui.services;
import com.example.ia_fxgui.PopupManager;

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

    public static double evaluate(String expression, double x) {
        TreeNode root = buildExpressionTree(expression);
        return evaluate(root, x);
    }

    public static double evaluate(TreeNode root, double x) {
        if (root == null) {
            throw new IllegalArgumentException("Invalid expression tree!");
        }

        if (!isOperator(root.value.charAt(0))) {
            if (root.value.charAt(0) == 'x') {
                return x;
            }
            return Double.parseDouble(root.value);
        }

        double leftValue = evaluate(root.left, x);
        double rightValue = evaluate(root.right, x);

        if (root.value.equals("^")) {
            return Math.pow(leftValue, rightValue);
        }

        return performOperation(root.value.charAt(0), leftValue, rightValue);
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static double performOperation(char operator, double operand1, double operand2) {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    PopupManager.getInstance().openPopup("Division by zero!");
                    throw new ArithmeticException("Division by zero!");
                }
                return operand1 / operand2;
            default:
                PopupManager.getInstance().openPopup("Invalid operator: " + operator);
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
