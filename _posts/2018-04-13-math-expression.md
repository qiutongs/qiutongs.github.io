---
title: "Math Expression"
categories: computer-science stack
permalink: /:categories/math-expression
---

## 前言
- 计算机/计算器是如何对数学表达式求值（evaluate）的呢？
- Leetcode上有个Reverse Polish Notation，是什么意思？
- 数学表达式可以有三种形式并且互相转换？
- 操作符（operator）和操作数（operand）？

## 我的抽象理解
我把数学表达式的抽象构成看成两个方面：

1. E1 X E2 (X 代表任意操作符, E1，E2 代表子表达式)  
2. 计算的优先级 
 - 括号把表达式分成了多个层级（level），从最内的层级向外面计算
 - 操作符的优先级

## 三种形式
简单例子
- infix: 1 + 2
- prefix：+ 1 2
- postfix：1 2 +

复杂例子
- infix: ( ( 15 / ( 7 - ( 1 + 1 ) ) ) * 3 ) - ( 2 + ( 1 + 1 ) )
- prefix：- * / 15 - 7 + 1 1 3 + 2 + 1 1
- postfix：15 7 1 1 + - / 3 * 2 1 1 + + -

> 很容易让人想到树的遍历（tree traverse）

### prefix
学名叫Polish notation(PN)。操作符放在操作数的前面，所以叫prefix。
<br>手动转化成infix：(5-6)*7 = *(-56)7 = *-567

### postfix
学名叫Reverse Polish notation(RPN)。操作符放在操作数的后面，所以叫postfix。
<br>手动转化成infix：(5-6)*7 = (56-)7* = 56-7*

### 精髓
prefix和postfix的精髓在于表达式**不依赖括号的无歧义性**。计算的优先级已经由操作符和操作数的顺序体现出来了。

## 求值(evaluate) 算法和转换算法
其中两个简单辅助函数的代码省略 isOperator， evaluate

### prefix evaluate
算法一：从左向右。时间O(n), 空间O(n)

{% include src/evalPN.java %}

算法二：从右向左
类似于Reverse Polish Notation的从左向右，代码省略。

### postfix evaluate

算法一：从左向右。 时间O(n), 空间O(n)

{% include src/evalRPN.java %}

算法二：从右向左
类似于Polish Notation的从左向右，代码省略。

### infix to postfix
在infix 算法求值这里要先提一下infix->postfix的shunting-yard算法（Edsger Dijkstra）。

{% include src/infixToPostfix.java %}

### infix evaluate
这个可以由infix->postfix的shunting-yard算法改变而成

{% include src/evalInfix.java %}

### infix to prefix

算法总共分三步

- 反转原infix表达式, 但是要替换'(' 和 ')'。 A*（B+C) -> )C+B(*A -> (C+B)*A
- 转化为postfix形式 infixToPost (C+B)*A -> CB+A*
- 反转postfix形式，即得到prefixxingshi CB+A* -> *A+BC

### 我对这些算法的思考

> prefix和postfix形式都可以保持操作数的顺序不变，而只是在合适的位置放入操作符。

> postfix evaluate算法比prefix evaluate算法要简单，我认为有两个原因。（1）在prefix中，要跟踪第一个操作数和操作符，而postfix只需要跟踪两个操作数 （2）在求值发生了一次后，操作数stack产生了一个新的操作数。这时prefix需要向前查找因为新的操作数触发了成了更多的求值机会，而postfix只需要向后等待操作符的出现。

> shunting-yard的思路。一个操作符的优先级由它**右边的符号（操作符或括号）所决定**。而操作符stack完美解决了所有优先级的问题。（1）括号优先级：先pop括号里的操作符，再pop括号外的（2）操作符优先级：先pop优先级一定大的操作符，直到表达式的最后。

> infix to postfix。可以明显看出这个算法的核心完全是shunting yard。只不过在shunting yard把操作符放入输出队列的时候，这个算法直接求值，然后把得到的操作数放入操作数栈。

> infix to prefix。数学里一个重要思想就是把未知问题转化成已知问题。要得到+AB，可以得到BA+再反转。所以把原表达式先反转BA，就可以了。

> 我认为shunting yard是无法把infix直接转化为prefix的，因为infix有可能需要在表达式中间插入新操作符，而postfix只需要在表达式后面append。
<br>举一个例子：E1 + E2 / E3 (E1,E2,E3代表三个子表达式)，为了形成 + E1 / E2 E3 prefix需要在E1和E2中间插入'/'。这就需要对**整个子表达式**进行跟踪，而不仅仅是对操作数进行跟踪。stackoverflow上有这样一个算法。

## Reference
- [Polish Notation wiki](https://en.wikipedia.org/wiki/Polish_notation)
- [GFG evaluate prefix](https://www.geeksforgeeks.org/evaluation-prefix-expressions/)
- [Reverse Polish Notation wiki](https://en.wikipedia.org/wiki/Reverse_Polish_notation)
- [Leetcode RPN](https://leetcode.com/problems/evaluate-reverse-polish-notation/description/)
- [Shunting-yard algorithm](https://en.wikipedia.org/wiki/Shunting-yard_algorithm)
- [GFG evaluate expression(infix)](https://www.geeksforgeeks.org/expression-evaluation/)
- [SOF infix to prefix](https://stackoverflow.com/questions/1946896/conversion-from-infix-to-prefix)
