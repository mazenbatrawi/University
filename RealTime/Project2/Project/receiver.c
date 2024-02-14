#include "header.h"

pid_t parent_pid;
struct MEMORY *memory;
int shmid;
int max_column, rows;
FILE *fp;
char ***words;
size_t len;
ssize_t reading;
char* line;
key_t key;
int msgQ_ID;

int random_int(int l, int r){
    return l + rand() % (r - l + 1);
}

void attach_and_access_shared_memory(){
    fp = fopen("max.txt", "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-75);
    }

    if(fscanf(fp, "%d %d", &max_column, &rows) != 2){
        fprintf(stderr, "Couldn't read the values from max.txt\n");
        exit(-76);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-77);
    }
    shmid = shmget(parent_pid, sizeof(struct MEMORY) * max_column, 0666 | IPC_CREAT);

    if(shmid < 0){
        fprintf(stderr, "Couldn't generate the ID of the shared memory\n");
        exit(-78);
    }

    memory = (struct MEMORY*) shmat(shmid, NULL, 0);

    if(memory == (void *) -1){
        fprintf(stderr, "Couldn't attach memory\n");
        exit(-79);
    }

    words = (char***) malloc(max_column * sizeof(char**));
    if(words == NULL){
        fprintf(stderr, "Couldn't allocate words\n");
        exit(-80);
    }

    for(int i = 0; i < max_column; i++){
        words[i] = (char**) malloc((rows + 3) * sizeof(char*));
        if(words[i] == NULL){
            fprintf(stderr, "Couldn't allocate words[i]\n");
            exit(-81);
        }
        for(int j = 0; j < rows + 3; j++){
            words[i][j] = (char*) malloc(M * sizeof(char));
            if(words[i][j] == NULL){
                fprintf(stderr, "Couldn't allocate words[i][j]\n");
                exit(-82);
            }
        }
    }
}

void decode(int k){
    int column_number = atoi(words[k][1]);
    for(int i = 3; i < rows + 3; i++){
        if(check_numerical(words[k][i])){
            if(atoi(words[k][i]) <= 29){
                char c = decode_symbol(atoi(words[k][i]));
                words[k][i][0] = c;
                words[k][i][1] = '\0';
            }
            else{
                int x = 1000000 - atoi(words[k][i]);
                sprintf(words[k][i], "%d", x);
            }
        }
        else{
            for(int j = 0; j < strlen(words[k][i]); j++){
                if(islower(words[k][i][j])){
                    int x = words[k][i][j] - 'a';
                    x -= column_number * (j + 1);
                    x %= 26;
                    if(x < 0) x += 26;
                    words[k][i][j] = (char) (x + 'a');
                }
                else{
                    int x = words[k][i][j] - 'A';
                    x -= column_number * (j + 1);
                    x %= 26;
                    if(x < 0) x += 26;
                    words[k][i][j] = (char) (x + 'A');
                }
            }
        }
    }
    printf("\033[1;32mThe Receiver decoded column %d\n\033[0m\n", column_number);
    fflush(stdout);
}

void send(){
    struct msqid_ds msq_stat;
    if (msgctl(msgQ_ID, IPC_STAT, &msq_stat) == -1) {
        exit(-2);
    }
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "receiver");
    d.value = 1;
    message.d = d;
    message.type = 1;
    if (msq_stat.msg_qnum + 3 < msq_stat.msg_qbytes / sizeof(struct msgQ_animation)) {
        if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
            fprintf(stderr, "Couldn't send to master\n");
        }
    }
}

void handle(int sig){    
    int seen[max_column];
    for(int i = 0; i < max_column; i++) seen[i] = 0;
    while(1){
        int has = 0;
        for(int i = 0; i < max_column; i++) has += seen[i];
        if(has == max_column) break;

        if(sem_wait(semaphore) == -1){
            fprintf(stderr, "Couldn't wait for semaphore\n");
            exit(-83);
        }

        int idx = random_int(0, max_column - 1);
        if(!memory[idx].done) {
            if(sem_post(semaphore) == -1){
                fprintf(stderr, "Couldn't post the semaphore\n");
                exit(-84);
            }
            continue;
        }

        int col = atoi(memory[idx].buffer[1]) - 1;
        if(seen[col]){
            if(sem_post(semaphore) == -1){
                fprintf(stderr, "Couldn't post the semaphore\n");
                exit(-85);
            }
            continue;
        }

        printf("\033[1;32mThe Receiver received column %d\n\033[0m\n", col + 1); 
        fflush(stdout);

        send();

        seen[col] = 1;
        for(int i = 0; i < rows + 3; i++){
            strcpy(words[col][i], memory[idx].buffer[i]);
        }

        if(sem_post(semaphore) == -1){
            fprintf(stderr, "Couldn't post the semaphore\n");
            exit(-86);
        }
    }

    for(int i = 0; i < max_column; i++) decode(i);

    if(file_exists("receiver.txt")){
        if(remove("receiver.txt") == -1){
            fprintf(stderr, "Couldn't remove existing receiver.txt\n");
            exit(-62);
        }
    }

    fp = fopen("receiver.txt", "a+");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open receiver.txt\n");
        exit(-87);
    }

    for(int i = 3; i < rows + 3; i++) {
        for(int j = 0; j < max_column; j++){
            if(j > 0){
                if(fprintf(fp, " ") < 0){
                    fprintf(stderr, "Couldn't write to receiver.txt\n");
                    exit(-88);
                }
            }
            if(fprintf(fp, "%s", words[j][i]) < 0){
                fprintf(stderr, "Couldn't write to receiver.txt\n");
                exit(-89);
            }
            if(j == max_column - 1){
                if(fprintf(fp, "\n") < 0){
                    fprintf(stderr, "Couldn't write to receiver.txt\n");
                    exit(-90);
                }
            }
        }
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file receiver.txt\n");
        exit(-91);
    }
    
    if(sem_close(semaphore) < 0){
        fprintf(stderr, "Receiver couldn't close the semaphore\n");
        sem_unlink("/read_write_semaphore");
        exit(-92);
    }

    printf("RECEIVER FINISHED\n");
    fflush(stdout);

    if(kill(getppid(), SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal from the receiver to the parent");
        exit(-1);
    }
}

int main(int argc, char **argv){
    if(argc != 1){
        fprintf(stderr, "Wrong usage\n");
        exit(-94);
    }

    parent_pid = getppid();
    srand(time(NULL) ^ getpid());
    attach_and_access_shared_memory();
    
    if(signal(SIGUSR1, handle) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function handle to a signal\n");
        exit(-95);
    }

    msgQ_ID = msgget(ID, 0666 | IPC_CREAT);

    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    semaphore = sem_open("/read_write_semaphore", 0);

    if (semaphore == SEM_FAILED) {
        perror("sem_open");
        sem_close(semaphore);
        sem_unlink("/read_write_semaphore");
        exit(-96);
    }

    if(kill(getppid(), SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the parent\n");
        exit(-97);
    }
    
    while(1){
        pause();
    }
    return 0;
}