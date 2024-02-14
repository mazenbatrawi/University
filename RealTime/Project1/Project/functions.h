#ifndef FUNCTIONS
#define FUNCTIONS

#include "header.h"

int check_numerical(char *num){ // check if the string is an integer
    for(int j = 0; j < strlen(num); j++){
        if(num[j] < '0' || num[j] > '9'){
            return 0;
        }
    }
    return 1;
}

#endif