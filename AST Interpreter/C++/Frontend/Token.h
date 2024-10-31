#pragma once

#include <string>
#include <variant>
#include "TokenType.h"

namespace Frontend
{

    using Literal = std::variant<std::nullptr_t, double, std::string>;

    class Token
    {
    public:
        TokenType type;
        std::string lexeme;
        Literal literal;
        int line;

        Token(TokenType type, const std::string &lexeme, const Literal &literal, int line)
            : type(type), lexeme(lexeme), literal(literal), line(line) {}

        std::string toString() const;
    };

} // namespace Frontend
