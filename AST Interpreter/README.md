```text
Language Definition:

program → declaration* EOF

declaration → varDecl | funDecl | statement;
varDecl → "var" IDENTIFIER ( "=" expression)? ";"
funDecl → "function" function
function → IDENTIFIER "(" parameters? ")" block;
parameters → IDENTIFIER ("," IDENTIFIER)* 

statement → exprStmt | printStmt | block | ifStmt | whileStmt | returnStmt
block → "{" declaration* "}"

ifStmt → "if" "(" expression ")" statement ( "else" statement )? 
whileStmt → "while" "(" expression ")" statement ;

exprStmt → expression ";"
printStmt → "print" expression ";"

expression → assignment
assignment → IDENTIFIER "=" assignment | logic_or
logic_or → logic_and ("or" logic_and)*
logic_and → equality ("and" equality)*
equality → comparison ( ("!=" | "==") comparison)*
equality → term ( (">" | ">=" | "<" | "<=") term)*
term → factor ( ("-" | "+") factor)*
factor → unary ( ("*" | "/") unary)*
unary → ("!" | "-") unary | call
call → primary (   "("  arguments?   ")"  )*
arguments → expression ("," expression)* 
primary → NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER

```
