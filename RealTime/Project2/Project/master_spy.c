#include "header.h"

char *key_string;
int key_value, msgQ_ID;
key_t key;
int max_column, rows;
char ***words;
FILE *fp;
int msgQ_ID_ANIMATION;

void get_max(){
    fp = fopen("max.txt", "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-54);
    }

    if(fscanf(fp, "%d %d", &max_column, &rows) != 2){
        fprintf(stderr, "Couldn't read the values from max.txt\n");
        exit(-55);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-56);
    }

    words = (char***) malloc(max_column * sizeof(char**));
    if(words == NULL){
        fprintf(stderr, "Couldn't allocate words\n");
        exit(-57);
    }

    for(int i = 0; i < max_column; i++){
        words[i] = (char**) malloc((rows + 3) * sizeof(char*));
        if(words[i] == NULL){
            fprintf(stderr, "Couldn't allocate words[i]\n");
            exit(-59);
        }
        for(int j = 0; j < rows + 3; j++){
            words[i][j] = (char*) malloc(M * sizeof(char));
            if(words[i][j] == NULL){
                fprintf(stderr, "Couldn't allocate words[i][j]\n");
                exit(-60);
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
    printf("\033[1;31mThe master decoded column %d\n\033[0m\n", column_number);
    fflush(stdout);
}

void send(){
    struct msqid_ds msq_stat;
    if (msgctl(msgQ_ID_ANIMATION, IPC_STAT, &msq_stat) == -1) {
        exit(-2);
    }
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "master_spy_rec");
    d.value = 1;
    message.d = d;
    message.type = 1;
    if (msq_stat.msg_qnum + 3 < msq_stat.msg_qbytes / sizeof(struct msgQ_animation)) {
        if(msgsnd(msgQ_ID_ANIMATION, &message, sizeof(message), 0) == -1){
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
        
        struct msqid_ds buf;
        if (msgctl(msgQ_ID, IPC_STAT, &buf) == -1) {
            perror("msgctl master spy");
            exit(-61);
        }

        if(buf.msg_qnum > 0){
            struct msgQ message;

            if(msgrcv(msgQ_ID, &message, sizeof(message), 1, 0) == -1){
                fprintf(stderr, "Couldn't receive message\n");
                exit(-61);
            }

            struct MEMORY mem = message.mem;
            int col = atoi(mem.buffer[1]) - 1;

            if(seen[col]){
                continue;
            }
            
            printf("\033[1;31mThe master received column %d\n\033[0m\n", col + 1);
            fflush(stdout);
            send();

            seen[col] = 1;

            for(int i = 0; i < rows + 3; i++){
                strcpy(words[col][i], mem.buffer[i]);
            }
        }
    }

    for(int i = 0; i < max_column; i++){
        decode(i);
    }

    if(file_exists("spy.txt")){
        if(remove("spy.txt") == -1){
            fprintf(stderr, "Couldn't remove existing spy.txt\n");
            exit(-62);
        }
    }

    fp = fopen("spy.txt", "a+");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open spy.txt\n");
        exit(-63);
    }

    for(int i = 3; i < rows + 3; i++) {
        for(int j = 0; j < max_column; j++){
            if(j > 0){
                if(fprintf(fp, " ") < 0){
                    fprintf(stderr, "Couldn't write to spy.txt\n");
                    exit(-64);
                }
            }
            if(fprintf(fp, "%s", words[j][i]) < 0){
                fprintf(stderr, "Couldn't write to spy.txt\n");
                exit(-65);
            }
            if(j == max_column - 1){
                if(fprintf(fp, "\n") < 0){
                    fprintf(stderr, "Couldn't write to spy.txt\n");
                    exit(-66);
                }
            }
        }
    }
    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file spy.txt\n");
        exit(-67);
    }
    if(msgctl(msgQ_ID, IPC_RMID, NULL) == -1){
        fprintf(stderr, "Couldn't close message queue\n");
        exit(-67);
    }

    printf("MASTER FINISHED\n");
    fflush(stdout);
    if(kill(getppid(), SIGUSR2) < 0){
        fprintf(stderr, "Couldn't send a signal from the master to the parent");
        exit(-1);
    }
}

int main(int argc, char **argv){
    if(argc != 3){
        fprintf(stderr, "Error usage\n");
        exit(-69);
    }

    if(file_exists("spy.txt")) {
        if(remove("spy.txt") == -1){
            fprintf(stderr, "Couldn't remove existing receiver.txt\n");
            exit(-62);
        }
    }

    key_string = argv[1];

    if(!check_numerical(argv[2])){
        fprintf(stderr, "argv[2] should be an integer\n");
        exit(-70);
    }

    key_value = atoi(argv[2]);

    msgQ_ID = msgget(ID2, 0666 | IPC_CREAT);
    msgQ_ID_ANIMATION = msgget(ID, 0666 | IPC_CREAT);

    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    if(msgQ_ID_ANIMATION == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    get_max();
    
    if(signal(SIGUSR1, handle) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function handle to a signal\n");
        exit(-73);
    }
    if(kill(getppid(), SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the parent\n");
        exit(-74);
    }

    while(1) pause();
    return 0;
}