#include "header.h"

int max_column, rows;
FILE *fp;
int shmid;
struct MEMORY *memory;
key_t key;
int msgQ_ID;

int random_int(int l, int r){
    return l + rand() % (r - l + 1);
}

void get_max(){
    fp = fopen("max.txt", "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-42);
    }

    if(fscanf(fp, "%d %d", &max_column, &rows) != 2){
        fprintf(stderr, "Couldn't read the values from max.txt\n");
        exit(-43);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-44);
    }

    shmid = shmget(getppid(), sizeof(struct MEMORY) * max_column, 0666 | IPC_CREAT);

    if(shmid < 0){
        fprintf(stderr, "Couldn't generate the ID of the shared memory\n");
        exit(-45);
    }

    memory = (struct MEMORY*) shmat(shmid, NULL, 0);

    if(memory == (void *) -1){
        fprintf(stderr, "Couldn't attach memory\n");
        exit(-46);
    }
}

void send_swap(){
    struct msqid_ds msq_stat;
    if (msgctl(msgQ_ID, IPC_STAT, &msq_stat) == -1) {
        exit(-2);
    }
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "swap");
    d.value = 1;
    message.d = d;
    message.type = 1;
    if (msq_stat.msg_qnum + 3 < msq_stat.msg_qbytes / sizeof(struct msgQ_animation)) {
        if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
            fprintf(stderr, "Couldn't send to master\n");
        }
    }
}


void swap(struct MEMORY memory1, struct MEMORY memory2) {
    struct MEMORY temp;

    memcpy(temp.buffer, memory1.buffer, sizeof(memory1.buffer));
    temp.done = memory1.done;

    memcpy(memory1.buffer, memory2.buffer, sizeof(memory2.buffer));
    memory1.done = memory2.done;

    memcpy(memory2.buffer, temp.buffer, sizeof(temp.buffer));
    memory2.done = temp.done;

    send_swap();

}

void handle(int sig){
    while(1){
        if(sem_wait(semaphore) == -1){
            fprintf(stderr, "Couldn't wait for semaphore\n");
            exit(-47);
        }

        int idx1 = random_int(0, max_column - 1);
        int idx2 = random_int(0, max_column - 1);
        swap(memory[idx1], memory[idx2]);

        if(sem_post(semaphore) == -1){
            fprintf(stderr, "Couldn't post the semaphore\n");
            exit(-48);
        }
    }
}

void stop(int sig){
    if(sem_close(semaphore) == -1){
        fprintf(stderr, "Couldn't close the semaphore\n");
        exit(-49);
    }
    exit(0);
}

int main(int argc, char **argv){
    if(argc != 1){
        fprintf(stderr, "Wrong usage\n");
        exit(-50);
    }

    msgQ_ID = msgget(ID, 0666 | IPC_CREAT);
    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    srand(time(NULL) ^ getpid());
    semaphore = sem_open("/read_write_semaphore", 0);

    if (semaphore == SEM_FAILED) {
        fprintf(stderr, "sem_open -- helper");
        sem_close(semaphore);
        sem_unlink("/read_write_semaphore");
        exit(-51);
    }

    if(signal(SIGUSR1, handle) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function handle to a signal\n");
        exit(-52);
    }

    if(signal(SIGUSR2, stop) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function stop to a signal\n");
        exit(-53);
    }
    
    get_max();
    
    while(1) pause();
    return 0;
}