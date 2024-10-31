#include "Token.h"
#include <sstream>

namespace Frontend
{

    std::string Token::toString() const
    {
        std::ostringstream oss;
        oss << tokenTypeToString(type) << " " << lexeme << " ";

        if (std::holds_alternative<std::nullptr_t>(literal))
        {
            oss << "nil";
        }
        else if (std::holds_alternative<double>(literal))
        {
            oss << std::get<double>(literal);
        }
        else if (std::holds_alternative<std::string>(literal))
        {
            oss << std::get<std::string>(literal);
        }
        else
        {
            oss << "unknown";
        }

        return oss.str();
    }

} // namespace Frontend
