Language Definition:

program → statement* EOF
statement → exprStmt | printStmt
exprStmt → expression ";"
printStmt → "print" expression ";"

expression → equality
equality → comparison ( ("!=" | "==") comparison)*
equality → term ( (">" | ">=" | "<" | "<=") term)*
term → factor ( ("-" | "+") factor)*
factor → ("!" | "-") unary | primary
primary → NUMBER | STRING | "true" | "false" | "nil" | "(" expression ")"



