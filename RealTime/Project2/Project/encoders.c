#include "header.h"

int column_number, num_words, shmid, max_column, rows, i, j, x;
char words[N][M];
pid_t parent_pid;
struct MEMORY *memory;
FILE *fp;

void encode(){
    for(i = 3; i < num_words; i++){
        if(check_numerical(words[i])){
            if(sprintf(words[i], "%d", 1000000 - atoi(words[i])) < 0){
                fprintf(stderr, "Couldn't encode column %d at word %d\n", column_number, i - 3 + 1);
                exit(-35);
            }
        }
        else if(strlen(words[i]) == 1 && !isalnum(words[i][0])){
            if(sprintf(words[i], "%d", encode_symbol(words[i][0])) < 0){
                fprintf(stderr, "Couldn't encode column %d at word %d\n", column_number, i - 3 + 1);
                exit(-36);
            }
        }
        else{
            for(j = 0; j < strlen(words[i]); j++){
                if(islower(words[i][j])){
                    x = words[i][j] - 'a';
                    x += column_number * (j + 1);
                    x %= 26;
                    words[i][j] = (char) (x + 'a');
                }
                else{
                    x = words[i][j] - 'A';
                    x += column_number * (j + 1);
                    x %= 26;
                    words[i][j] = (char) (x + 'A');
                }
            }
        }
    }
}

void attach_and_access_shared_memory(){
    fp = fopen("max.txt", "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-37);
    }

    if(fscanf(fp, "%d %d", &max_column, &rows) != 2){
        fprintf(stderr, "Couldn't read the values from max.txt\n");
        exit(-38);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-39);
    }

    shmid = shmget(parent_pid, sizeof(struct MEMORY) * max_column, 0666 | IPC_CREAT);

    if(shmid < 0){
        fprintf(stderr, "Couldn't generate the ID of the shared memory\n");
        exit(-40);
    }

    memory = (struct MEMORY*) shmat(shmid, NULL, 0);

    if(memory == (void *) -1){
        fprintf(stderr, "Couldn't attach memory\n");
        exit(-41);
    }
}

void write_to_shared_memory(){
    if(sem_wait(semaphore) == -1){
        fprintf(stderr, "Couldn't wait for semaphore\n");
        exit(-42);
    }

    int idx = -1;
    for(int i = 0; i < max_column; i++){
        if(memory[i].done == 0){
            idx = i;
            break;
        }
    }

    assert(idx != -1);

    for(int i = 0; i < num_words; i++){
        strcpy(memory[idx].buffer[i], words[i]);
    }

    memory[idx].done = 1;

    if(sem_post(semaphore) == -1){
        fprintf(stderr, "Couldn't post the semaphore\n");
        exit(-43);
    }

    if(sem_close(semaphore) == -1){
        fprintf(stderr, "Couldn't close the semaphore\n");
        exit(-44);
    }
}

int main(int argc, char **argv) {
    if(argc < 3){
        fprintf(stderr, "Wrong usage\n");
        exit(-45);
    }

    semaphore = sem_open("/read_write_semaphore", 0);

    if (semaphore == SEM_FAILED) {
        fprintf(stderr, "semaphore - encoder");
        sem_close(semaphore);
        sem_unlink("/read_write_semaphore");
        exit(-46);
    }

    if(!check_numerical(argv[1])){
        fprintf(stderr, "Number of the column is not a positive integer");
        exit(-47);
    }

    if(!check_numerical(argv[2])){
        fprintf(stderr, "Parent PID positive integer");
        exit(-47);
    }

    column_number = atoi(argv[1]);
    parent_pid = atoi(argv[2]);
    num_words = argc;

    printf("The encoder %d received the column: ", getpid());
    fflush(stdout);
    for(int i = 0; i < argc; i++){
        strcpy(words[i], argv[i]);
        if(i >= 3){
            printf("%s ", words[i]);
            fflush(stdout);
        }
    }
    printf("\n\n");
    fflush(stdout);
    
    attach_and_access_shared_memory();
    encode();
    write_to_shared_memory();
    return 0;
}