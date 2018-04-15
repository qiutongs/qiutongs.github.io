{% highlight java linenos %}
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
{% endhighlight %}