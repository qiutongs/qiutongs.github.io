{% highlight java linenos %}
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

{% endhighlight %}