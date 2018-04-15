{% highlight java linenos %}
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
{% endhighlight %}