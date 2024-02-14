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

int random_range(int l, int r){
    return rand() % (r - l + 1) + l;
}

float dist(float x1, float y1, float x2, float y2){
    return sqrtf((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
}

int max(int a, int b){
    return a >= b ? a : b;
}

int hit(float x, float y){
    return x  >= BORDER || x <= -BORDER || y >= BORDER || y  <= -BORDER;
}