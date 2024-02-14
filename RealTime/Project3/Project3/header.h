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

#define BORDER 0.95f

#include "functions.h"

struct ANT {
    float speedX, speedY, circleX, circleY, radius;
    float toX, toY;
    float curr_pheromone;
    int toIdx;
    int dir;
};

struct FOOD {
    float fillingX, fillingY, crustX, crustY;
    int portion;
    pthread_mutex_t lock;
    int id;
};

#endif