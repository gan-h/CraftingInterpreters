Language Definition:

program → declaration* EOF

declaration → varDecl | statement;
varDecl → "var" IDENTIFIER ( "=" expression)? ";"

statement → exprStmt | printStmt
exprStmt → expression ";"
printStmt → "print" expression ";"

expression → assignment
assignment → IDENTIFIER "=" assignment | equality
equality → comparison ( ("!=" | "==") comparison)*
equality → term ( (">" | ">=" | "<" | "<=") term)*
term → factor ( ("-" | "+") factor)*
factor → ("!" | "-") unary | primary
primary → NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")" | IDENTIFIER



