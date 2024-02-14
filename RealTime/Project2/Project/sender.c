#include "header.h"

FILE *fp, *fp2;
char *file_name, *file_name2, *file_name3, *file_name4, *line, ***columns;
size_t len;
ssize_t reading;
int i, j, max_number_of_columns, rows;
pid_t *encoders_array;
int parent_pid;
struct MEMORY *memory;
key_t key;
int msgQ_ID;

void separateLine(char* str) {
    i = 0;
    len = strlen(str);
    char separatedLine[3 * (int) len + 5];
    
    j = 0;
    while (str[i] != '\0') {
        if (ispunct(str[i])) {
            if (i > 0 && !isspace(str[i - 1])) {
                separatedLine[j++] = ' ';
            }
            
            separatedLine[j++] = str[i];
            
            if (str[i + 1] != '\0' && !isspace(str[i + 1])) {
                separatedLine[j++] = ' ';
            }
        }
        else if(isdigit(str[i])){
            if (i > 0 && !isspace(str[i - 1])) {
                separatedLine[j++] = ' ';
            }
            
            int start = i;

            while (str[i + 1] != '\0' && (isdigit(str[i + 1]))) {
                i++;
            }
            
            int end = i;
            
            for (int k = start; k <= end; k++) {
                separatedLine[j++] = str[k];
            }
            
            if (str[i + 1] != '\0' && !isspace(str[i + 1])) {
                separatedLine[j++] = ' ';
            }
        }
        else{
            separatedLine[j++] = str[i];
        }
        
        i++;
    }
    
    separatedLine[j] = '\0';
    strcpy(str, separatedLine);
}

void preprocess_file(){
    char *command = (char*) malloc(100 * sizeof(char));
    sprintf(command, "sed -i 's/^ *//;s/ *$//;s/  */ /g' %s", file_name);
    system(command);
    sprintf(command, "fmt -s -w 80 %s > %s", file_name, file_name2);
    system(command);
    FILE *input_file = fopen(file_name2, "r");
    if(file_exists(file_name4)) remove(file_name4);
    FILE *output_file = fopen(file_name4, "a+");
    if(input_file == NULL){
        perror("ERROR");
        exit(-2);
    }
    char line[1000000];
    while (fgets(line, sizeof(line), input_file) != NULL) {
        if(line[0] == '\n') continue;
        separateLine(line);
        fprintf(output_file, "%s", line);
    }
    if(fclose(input_file) != 0 || fclose(output_file) != 0){
        perror("couldn't close files");
        exit(-2);
    }
    sprintf(command, "sed -i 's/^ *//;s/ *$//;s/  */ /g' %s", file_name4);
    system(command);
    sprintf(command, "fmt -s -w 80 %s > %s", file_name4, file_name2);
    system(command);
}

int max(int a, int b){
    return a >= b ? a : b;
}

void get_max_line(){
    max_number_of_columns = 0;

    fp = fopen(file_name2, "r");

    if(fp == NULL){
        fprintf(stderr, "The sender couldn't open the file %s\n", file_name2);
        exit(-101);
    }

    max_number_of_columns = 0;
    rows = 0;

    while(((reading = getline(&line, &len, fp))) != -1){
        rows++;
        int words = 1;
        
        for(i = 0; i < strlen(line); i++){
            words += (line[i] == ' ');
        }

        max_number_of_columns = max(max_number_of_columns, words);
        len = 0;
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file %s\n", file_name2);
        exit(-102);
    }
}

void append_alright(){
    FILE* input_file = fopen(file_name2, "r");
    if(file_exists(file_name3)) remove(file_name3);
    FILE* output_file = fopen(file_name3, "a+");

    if (input_file == NULL || output_file == NULL) {
        printf("Error opening files.\n");
        exit(-1);
    }

    char line[200];
    while (fgets(line, sizeof(line), input_file) != NULL) {
        if(line[0] == '\n') continue;
        int word_count = 1;
        for(int i = 0; i < strlen(line); i++) word_count += line[i] == ' ';
        if(word_count < max_number_of_columns){
            line[strlen(line) - 1] = '\0';
            while (word_count < max_number_of_columns) {
                strcat(line, " alright");
                word_count++;
            }
            strcat(line, "\n");
        }
        
        fprintf(output_file, "%s", line);
    }
    if(fclose(input_file) != 0 || fclose(output_file) != 0){
        perror("couldn't close files");
        exit(-2);
    }
}

void generate_encoders(){
    encoders_array = (pid_t*) malloc(max_number_of_columns * sizeof(pid_t));

    if(encoders_array == NULL){
        fprintf(stderr, "Couldn't generate the encoders array\n");
        exit(-107);
    }

    for(i = 0; i < max_number_of_columns; i++){
        sleep(1);
        encoders_array[i] = fork();

        if(encoders_array[i] < 0){
            fprintf(stderr, "Couldn't generate encoder %d\n", i + 1);
            exit(-108);
        }
        else if(encoders_array[i] == 0){
            execvp("./encoders", columns[i]);
            fprintf(stderr, "Shouldnt be here\n");
            exit(-109);
        }
        else{
            printf("\nGenerated encoder %d successfully, it's pid is: %d\n", i + 1, encoders_array[i]);
        }
    }
}

void generate_columns() {
    columns = (char***) malloc(max_number_of_columns * sizeof(char**));
    if (columns == NULL) {
        fprintf(stderr, "Memory allocation failed for columns.\n");
        exit(-110);
    }

    for (int i = 0; i < max_number_of_columns; i++) {
        columns[i] = (char**) malloc((rows + 4) * sizeof(char*));
        if (columns[i] == NULL) {
            fprintf(stderr, "Memory allocation failed for column %d.\n", i + 1);
            for (int j = 0; j < i; j++) {
                free(columns[j]);
            }
            free(columns);
            exit(-111);
        }
    }

    fp = fopen(file_name3, "r");
    if (fp == NULL) {
        fprintf(stderr, "Failed to open file: %s\n", file_name3);
        for (int i = 0; i < max_number_of_columns; i++) {
            free(columns[i]);
        }
        free(columns);
        exit(-112);
    }

    for(j = 0; j < max_number_of_columns; j++){
        columns[j][0] = "encoders";
        char val[100];
        sprintf(val, "%d", j + 1);
        columns[j][1] = strdup(val);
        char val2[100];
        sprintf(val2, "%d", parent_pid);
        columns[j][2] = strdup(val2);
    }

    char* line = NULL;
    size_t line_buffer_size = 0;
    i = 0;
    j = 3;

    while(((reading = getline(&line, &len, fp))) != -1){
        line[strcspn(line, "\n")] = '\0';

        char* token = strtok(line, " ");
        while (token != NULL) {
            columns[i][j] = strdup(token);
            
            if (columns[i][j] == NULL) {
                fprintf(stderr, "Memory allocation failed for column %d, row %d.\n", i, j);
                for (int k = 0; k < max_number_of_columns; k++) {
                    for (int l = 0; l < j; l++) {
                        free(columns[k][l]);
                    }
                    free(columns[k]);
                }
                free(columns);
                fclose(fp);
                free(line);
                exit(-113);
            }

            i = (i + 1) % max_number_of_columns;
            if (i == 0) {
                j++;
                if (j == rows + 3) {
                    if(fclose(fp) != 0){
                        fprintf(stderr, "Couldn't close the file %s\n", file_name3);
                        exit(-114);
                    }
                    free(line);
                    break;
                }
            }

            token = strtok(NULL, " ");
        }
    }

    for(j = 0; j < max_number_of_columns; j++){
        columns[j][rows + 3] = NULL;
    }
}

void send_encoders(){
    struct msqid_ds msq_stat;
    if (msgctl(msgQ_ID, IPC_STAT, &msq_stat) == -1) {
        exit(-2);
    }
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "encoders");
    d.value = max_number_of_columns;
    message.d = d;
    message.type = 1;
    if (msq_stat.msg_qnum + 3 < msq_stat.msg_qbytes / sizeof(struct msgQ_animation)) {
        if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
            fprintf(stderr, "Couldn't send to master\n");
        }
    }
}

void do_file_processing(int sig){
    preprocess_file();
    get_max_line();
    send_encoders();
    append_alright();

    fp = fopen("max.txt", "w");
    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-115);
    }

    if(fprintf(fp, "%d %d", max_number_of_columns, rows) < 0){
        fprintf(stderr, "Couldn't print the values to max.txt\n");
        exit(-116);
    }
    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-117);
    }

    generate_columns();

    if(kill(getppid(), SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the parent\n");
        exit(-118);
    }
}

void encoders(int sig){
    generate_encoders();
    sleep(1);
    if(kill(getppid(), SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal from the sender to the parent");
        exit(-1);
    }
}

void done(int sig){
    for(int i = 0; i < max_number_of_columns; i++){
        if(kill(encoders_array[i], SIGKILL) < 0){
            fprintf(stderr, "Couldn't send a signal to the encoder\n");
            exit(-122);
        }
    }
    exit(0);
}

int main(int argc, char **argv){
    if(argc != 5){
        fprintf(stderr, "Error usage\n");
        exit(-119);
    }

    parent_pid = getppid();
    file_name = argv[1];
    file_name2 = argv[2];
    file_name3 = argv[3];
    file_name4 = argv[4];

    msgQ_ID = msgget(ID, 0666 | IPC_CREAT);
    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    if(signal(SIGUSR1, do_file_processing) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function do_file_processing to a signal\n");
        exit(-120);
    }

    if(signal(SIGUSR2, encoders) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function encoders to a signal\n");
        exit(-121);
    }

    if(signal(SIGPOLL, done) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function done to a signal\n");
        exit(-121);
    }

    if(kill(getppid(), SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the parent\n");
        exit(-122);
    }
    while(1) pause();
    return 0;
}
