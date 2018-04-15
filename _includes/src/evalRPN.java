{% highlight java linenos %}
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

{% endhighlight %}