package com.qiutongs.test;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class MathNotation {

    static Map<String, Integer> OPERATORS_MAP = new HashMap<>();

    static {
        OPERATORS_MAP.put("+", 1);
        OPERATORS_MAP.put("-", 1);
        OPERATORS_MAP.put("*", 1);
        OPERATORS_MAP.put("/", 1);
    }

    public static int evaluateInfix(String[] tokens) {
        // String[] tokens = expression.split("\\s+");
        Stack<Integer> operandStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (token.matches("\\d+")) {
                operandStack.push(Integer.valueOf(token));
            } else {
                if (isOperator(token)) {
                    while ((!operatorStack.isEmpty()) && shouldPopOperator(operatorStack.peek(), token)) {
                        processStack(operandStack, operatorStack);
                    }
                    operatorStack.push(token);
                } else if (token.equals("(")) {
                    operatorStack.push(token);
                } else if (token.equals(")")) {
                    while (!operatorStack.peek().equals("(")) {
                        processStack(operandStack, operatorStack);
                    }
                    operatorStack.pop();
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            processStack(operandStack, operatorStack);
        }

        return operandStack.peek();
    }

    private static void processStack(Stack<Integer> operandStack, Stack<String> operatorStack) {
        String operator = operatorStack.pop();
        int op2 = operandStack.pop();
        int op1 = operandStack.pop();
        operandStack.push(evaluate(op1, op2, operator));
    }

    public static String infixToPostfix(String[] tokens) {

        Queue<String> outputQueue = new LinkedList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (token.matches("\\d+")) { //token is an integer
                outputQueue.offer(token);
            } else {
                if (isOperator(token)) {
                    while ((!operatorStack.isEmpty()) && shouldPopOperator(operatorStack.peek(), token)) {
                        outputQueue.offer(operatorStack.pop());
                    }
                    operatorStack.push(token);
                } else if (token.equals("(")) {
                    operatorStack.push(token);
                } else if (token.equals(")")) {
                    while (!operatorStack.peek().equals("(")) {
                        outputQueue.offer(operatorStack.pop());
                    }
                    operatorStack.pop();
                }
            }
        }

        while (!operatorStack.isEmpty()) {
            outputQueue.offer(operatorStack.pop());
        }

        StringBuilder sb = new StringBuilder();
        outputQueue.forEach(token -> sb.append(token).append(" "));
        return sb.toString();
    }

    private static boolean shouldPopOperator(String topToken, String currentOperator) {
        if (topToken.equals("(")) {
            return false;
        } else if (isOperator(topToken)) {
            return OPERATORS_MAP.get(topToken).intValue() >= OPERATORS_MAP.get(currentOperator).intValue();
        } else {
            throw new RuntimeException();
        }
    }

    // Java implementation of https://en.wikipedia.org/wiki/Polish_notation
    public static int evalPN(String[] tokens) {
        // assume expression is valid

        //this true means we are looking for the second operator
        boolean toFindSecondOperand = false; 
        //because it is left-to-right scan 
        //and only until second operator is found should start evaluate,
        //we need two places to keep track of both operator and operand
        Stack<String> operatorStack = new Stack<>();
        Stack<Integer> operandStack = new Stack<>();

        //In total, number of operand = number of operator + 1
        for (String token : tokens) {
            if (isOperator(token)) {
                operatorStack.push(token);
                toFindSecondOperand = false;
            }  else {
                if (toFindSecondOperand) {
                    int op2 = Integer.valueOf(token);
                    while (!operandStack.isEmpty()) {
                        int op1 = operandStack.pop();
                        //it is safe to pop operatorStack without check empty
                        //because operator must appear before operand
                        String operator = operatorStack.pop();
                        op2 = evaluate(op1, op2, operator);
                    }
                    //At this point, operandStack is empty,
                    //if operator stack is not empty, 
                    //it indicates more operands will appear
                    //And keep toFindSecondOperand as 'true'
                    operandStack.push(op2);
                } else {
                    operandStack.push(Integer.valueOf(token));
                    toFindSecondOperand = true;
                }
            }
        }

        return operandStack.peek();
    }

    // Java implementation of https://en.wikipedia.org/wiki/Reverse_Polish_notation
    public static int evalRPN(String[] tokens) {
        // assume expression is valid

        Stack<Integer> operandsStack = new Stack<Integer>();

        for (String token : tokens) {
            if (isOperator(token)) {
                //it is safe to pop two operands,
                //otherwise it is invalid expression
                Integer i2 = operandsStack.pop();
                Integer i1 = operandsStack.pop();
                operandsStack.push(evaluate(i1, i2, token));
            } else {
                operandsStack.push(Integer.valueOf(token));
            }
        }
        return operandsStack.peek();
    }
    
    private static boolean isOperator(String x) {
        return OPERATORS_MAP.keySet().contains(x);
    }

    private static int evaluate(int op1, int op2, String operator) {
        switch (operator) {
        case "+":
            return op1 + op2;
        case "-":
            return op1 - op2;
        case "*":
            return op1 * op2;
        case "/":
            return op1 / op2;
        default:
            throw new RuntimeException();
        }
    }
}
