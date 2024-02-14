#ifndef __LIBRARIES__
#define __LIBRARIES__

#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <sys/types.h>
#include <signal.h>
#include <string.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <limits.h>
#include <sys/ipc.h>
#include <time.h>
#include <sys/sem.h>
#include <semaphore.h>
#include <pthread.h>
#include <sys/shm.h>
#include <errno.h>
#include <GL/glut.h>
#include <assert.h>
#include <sys/time.h>
#include <math.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/shm.h>
#include <sys/msg.h>
#include <stdbool.h>
#include <GL/glut.h>

#include "functions.h"

#define N 30
#define M 20
#define ID 0x123
#define ID2 0x456

struct MEMORY{
    char buffer[N][M];
    int  done;
};

sem_t *semaphore;

struct msgQ {
    long type;
    struct MEMORY mem;
};

struct data {
    char key[M];
    int value;
    double time;
};

struct msgQ_animation {
    long type;
    struct data d;
};

#endif