Aida Zhumabekova
CS 211
Assignment 3

First, lets check that program accepts numbers and booleans and procedures and prints stack
PS> 1
PS<1> true
PS<2> { dup mul }
PS<3> pstack
{  dup mul }
true
1.0

It doesn't accept random strings 
PS<3> sklhl
PS<3> jkbwc 3267 sjdhbc
PS<4> pstack
3267.0
{  dup mul }
true
1.0

Now lets check arithmetic operations and simple def operator
PS> 2
PS<1> 3
PS<2> add
PS<1> pstack
5.0
PS<1> 3
PS<2> sub
PS<1> pstack
2.0
PS<1> /a 4 def
PS<1> ptable
a=4.0

PS<1> a
PS<2> mul
PS<1> pstack
8.0
PS<1> a
PS<2> div
PS<1> pstack
2.0

Also this gives an error
PS> true false
PS<2> pstack
false
true
PS<2> add
Can only perform this command with two numbers!
PS<2> sub
Can only perform this command with two numbers!
PS<2> mul
Can only perform this command with two numbers!
PS<2> div
Can only perform this command with two numbers!

Pop the Token and check for dup, exch, eq, ne, not
PS<1> pop
PS> true
PS<1> dup
PS<2> not
PS<2> pstack
false
true
PS<2> exch
PS<2> pstack
true
false
PS<2> eq
PS<1> pstack
false
PS<1> false
PS<2> eq
PS<1> pstack
true
PS<1> true
PS<2> ne
PS<1> pstack
false
PS<1> true
PS<2> ne
PS<1> pstack
true

Now check def operations, so we start over
PS> /a 4 def
PS> /b { dup mul } def
PS> ptable
b={  dup mul }
a=4.0

PS> a b
PS<1> pstack
16.0
PS<1> b
PS<1> pstack
256.0
PS<1> /c 6
PS<3> pstack
6.0
/c
256.0
PS<3> def
PS<1> pstack
256.0
PS<1> ptable
c=6.0
b={  dup mul }
a=4.0



Start over and check if operations
PS> 2
PS<1> 3
PS<2> /a { 5 lt { -1 mul } if } def
PS<2> pstack
3.0
2.0
PS<2> a
PS<1> pstack
-2.0
PS<1> a
The stack must have at least two numbers!

Also, the if operator works without defining any symbol
PS> { true { 2 dup add } if }
PS<1> pstack
{  true {  2.0 dup add } if }
PS<1> true { 2 dup add } if 
PS<2> pstack
4.0
{  true {  2.0 dup add } if }
PS<2> 

Start over and check lt, gt, quit
PS>  2 3 lt
PS<1> pstack
true
PS<1> pop 
PS> 8 9 lt
PS<1> pstack
true
PS<1> quit

But this gives an error
PS<2> pstack
false
true
PS<2> gt
Can only perform this command with two numbers!


Let’s do an example from the book
PS> /pi 3.14 def
PS> /area { dup mul pi mul } def
PS> 1.6 area
PS<1> pstack
8.038400000000001
PS<1> 9 area pstack
254.34
8.038400000000001
PS<2> quit

PS> /abs { dup 0 lt { -1 mul } if } def
PS> 3 abs
PS<1> pstack
3.0
PS<1> -3 abs
PS<2> pstack
3.0
3.0
PS<2> eq pstack
true

PS> /count { dup 1 ne { dup 1 sub count } if } def
PS> 10 count pstack
1.0
2.0
3.0
4.0
5.0
6.0
7.0
8.0
9.0
10.0
PS<10> 



