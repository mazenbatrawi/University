#include "header.h"

int check_numerical(char *num){
    for(int j = 0; j < strlen(num); j++){
        if(num[j] < '0' || num[j] > '9'){
            return 0;
        }
    }
    return 1;
}

int file_exists (char *filename) {
    struct stat   buffer;   
    return (stat (filename, &buffer) == 0);
}

int encode_symbol(char c){
    if(c == '!') return 1;
    if(c == '?') return 2;
    if(c == ',') return 3;
    if(c == ';') return 4;
    if(c == '%') return 5;
    if(c == '@') return 6;
    if(c == '#') return 7;
    if(c == '$') return 8;
    if(c == '^') return 9;
    if(c == '&') return 10;
    if(c == '*') return 11;
    if(c == '(') return 12;
    if(c == ')') return 13;
    if(c == '-') return 14;
    if(c == '_') return 15;
    if(c == '=') return 16;
    if(c == '+') return 17;
    if(c == '\'') return 18;
    if(c == '\"') return 19;
    if(c == '/') return 20;
    if(c == '\\') return 21;
    if(c == '.') return 22;
    if(c == '<') return 23;
    if(c == '>') return 24;
    if(c == '{') return 25;
    if(c == '}') return 26;
    if(c == '[') return 27;
    if(c == ']') return 28;
    if(c == '|') return 29;
    return -1;
}

char decode_symbol(int c){
    if(c == 1 ) return '!';
    if(c == 2 ) return '?';
    if(c == 3 ) return ',';
    if(c == 4 ) return ';';
    if(c == 5 ) return '%';
    if(c == 6 ) return '@';
    if(c == 7 ) return '#';
    if(c == 8 ) return '$';
    if(c == 9 ) return '^';
    if(c == 10) return '&';
    if(c == 11) return '*';
    if(c == 12) return '(';
    if(c == 13) return ')';
    if(c == 14) return '-';
    if(c == 15) return '_';
    if(c == 16) return '=';
    if(c == 17) return '+';
    if(c == 18) return '\'';
    if(c == 19) return '\"';
    if(c == 20) return '/';
    if(c == 21) return '\\';
    if(c == 22) return '.';
    if(c == 23) return '<';
    if(c == 24) return '>';
    if(c == 25) return '{';
    if(c == 26) return '}';
    if(c == 27) return '[';
    if(c == 28) return ']';
    if(c == 29) return '|';
    return '1';
}