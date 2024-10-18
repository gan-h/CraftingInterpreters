#include <iostream>

using namespace std;

void runFile()
{
}

void runPrompt()
{
}

int main(int argc, char **argv)
{
    if (argc > 2)
    {
        cout << "Correct usage: jlox [filepath]" << endl;
    }
    else if (argc == 2)
    {
        runFile();
    }
    else
    {
        runPrompt();
    }

    return 0;
}