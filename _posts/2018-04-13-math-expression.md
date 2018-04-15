---
layout: "post"
title: "Math Expression"
categories: computer-science stack
permalink: /:categories/math-expression
---

# 前言
- 计算机/计算器是如何对数学表达式求值（evaluate）的呢？
- Leetcode上有个Reverse Polish Notation，是什么意思？
- 数学表达式可以有三种形式并且互相转换？
- 操作符（operator）和操作数（operand） ？

# 抽象理解
我认为数学表达式的抽象理解只有两个方面：
（1）E1 X E2 (X 代表任意操作符, E1，E2 代表子表达式)  
（2）计算的优先级 
    （2.1）括号把表达式分成了多个level，从最里面向外面计算
    （2.2）操作符的优先级

# 三种形式 （有点类似于tree traverse）
- infix: 1 + 2
- prefix：+ 1 2
- postfix：1 2 +

## prefix
学名叫Polish notation(PN)。操作符放在操作数的前面，所以叫prefix。

手动转化成infix：(5-6)*7 = *(-56)7 = *-567

复杂例子：- * / 15 - 7 + 1 1 3 + 2 + 1 1

## postfix
学名叫Reverse Polish notation(RPN)。操作符放在操作数的后面，所以叫postfix。

手动转化成infix：(5-6)*7 = (56-)7* = 56-7*

复杂例子： 15 7 1 1 + - / 3 * 2 1 1 + + -

## Infix
复杂例子： ( ( 15 / ( 7 - ( 1 + 1 ) ) ) * 3 ) - ( 2 + ( 1 + 1 ) )

## 精髓
prefix和postfix的精髓在于表达式不依赖括号的无歧义性。计算的优先级已经由操作符和操作数的顺序体现出来了。

# 求值(evaluate) 算法
用java实现了wikipedia上的算法。（其中两个简单辅助函数的代码省略 isOperator， evaluate）

## prefix
算法一：从左向右。时间O(n), 空间O(n)

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

算法二：从右向左
类似于Reverse Polish Notation的从左向右，代码省略。

## postfix
这是leetcode的原题。用java实现了wikipedia上的算法。其中两个简单辅助函数的代码省略 isOperator， evaluate）

算法一：从左向右。 时间O(n), 空间O(n)
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

算法二：从右向左
类似于Polish Notation的从左向右，代码省略。

## infix
这里要先提一下infix->postfix的shunting-yard算法（Edsger Dijkstra）。

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



这个可以由infix->postfix的shunting-yard算法改变而成

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

##


# Reference
- [Polish Notation wiki](https://en.wikipedia.org/wiki/Polish_notation)
- [GFG evaluate prefix](https://www.geeksforgeeks.org/evaluation-prefix-expressions/)
- [Reverse Polish Notation wiki](https://en.wikipedia.org/wiki/Reverse_Polish_notation)
- [Leetcode RPN](https://leetcode.com/problems/evaluate-reverse-polish-notation/description/)
- [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
