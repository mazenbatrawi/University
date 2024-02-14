#include "header.h"

int max_column, rows;
FILE *fp;
int shmid;
struct MEMORY *memory;
char *key_string;
int key_value, msgQ_ID, msgQ_ID_ANIMATION;
key_t key;

int random_int(int l, int r){
    return l + rand() % (r - l + 1);
}

void get_max(){
    fp = fopen("max.txt", "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-71);
    }

    if(fscanf(fp, "%d %d", &max_column, &rows) != 2){
        fprintf(stderr, "Couldn't read the values from max.txt\n");
        exit(-123);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-124);
    }

    shmid = shmget(getppid(), sizeof(struct MEMORY) * max_column, 0666 | IPC_CREAT);

    if(shmid < 0){
        fprintf(stderr, "Couldn't generate the ID of the shared memory\n");
        exit(-125);
    }

    memory = (struct MEMORY*) shmat(shmid, NULL, 0);

    if(memory == (void *) -1){
        fprintf(stderr, "Couldn't attach memory\n");
        exit(-126);
    }
}

void send(){
    struct msqid_ds msq_stat;
    if (msgctl(msgQ_ID_ANIMATION, IPC_STAT, &msq_stat) == -1) {
        exit(-2);
    }
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "spy_sent");
    d.value = 1;
    message.d = d;
    message.type = 1;
    if (msq_stat.msg_qnum + 3 < msq_stat.msg_qbytes / sizeof(struct msgQ_animation)) {
        if(msgsnd(msgQ_ID_ANIMATION, &message, sizeof(message), 0) == -1){
            fprintf(stderr, "Couldn't send to animator\n");
        }
    }
}

void handle(int sig){
    while(1){
        if(sem_wait(semaphore) == -1){
            fprintf(stderr, "Couldn't wait for semaphore\n");
            exit(1);
        }
        int idx = random_int(0, max_column - 1);
        if(!memory[idx].done){
            if(sem_post(semaphore) == -1){
                fprintf(stderr, "Couldn't post the semaphore\n");
                exit(3);
            }
            continue;
        }
        struct MEMORY data;
        memcpy(data.buffer, memory[idx].buffer, sizeof(memory[idx].buffer));
        struct msgQ message;
        message.mem = data;
        message.type = 1;

        struct msqid_ds msq_stat;
        if (msgctl(msgQ_ID, IPC_STAT, &msq_stat) == -1) {
            if(sem_post(semaphore) == -1){
                fprintf(stderr, "Couldn't post the semaphore\n");
                exit(3);
            }
            exit(-2);
        }

        // Check the current number of messages in the queue and compare it with the maximum
        if (msq_stat.msg_qnum < msq_stat.msg_qbytes / sizeof(struct msgQ)) {
            if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
                fprintf(stderr, "Couldn't send to master\n");
            }
        }

        send();
        
        if(sem_post(semaphore) == -1){
            fprintf(stderr, "Couldn't post the semaphore\n");
            exit(3);
        }
    }
}

void stop(int sig){
    if(sem_close(semaphore) == -1){
        fprintf(stderr, "Couldn't close the semaphore\n");
        exit(5);
    }
    exit(0);
}

int main(int argc, char **argv){
    if(argc != 3){
        fprintf(stderr, "Error usage\n");
        exit(6);
    }

    srand(time(NULL) ^ getpid());
    semaphore = sem_open("/read_write_semaphore", 0);

    if (semaphore == SEM_FAILED) {
        perror("sem_open");
        sem_close(semaphore);
        sem_unlink("/read_write_semaphore");
        exit(7);
    }

    key_string = argv[1];

    if(!check_numerical(argv[2])){
        fprintf(stderr, "argv[2] should be an integer\n");
        exit(8);
    }

    msgQ_ID = msgget(ID2, 0666 | IPC_CREAT);
    msgQ_ID_ANIMATION = msgget(ID, 0666 | IPC_CREAT);

    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(9);
    }
    if(msgQ_ID_ANIMATION == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(9);
    }

    if(signal(SIGUSR1, handle) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function handle to a signal\n");
        exit(10);
    }
    if(signal(SIGUSR2, stop) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function stop to a signal\n");
        exit(11);
    }

    get_max();
    while(1) pause();
    return 0;
}