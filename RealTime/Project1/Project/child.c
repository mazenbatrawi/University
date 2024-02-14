#include "header.h"

int sig_value, i, p[2], my_pid, minimum, maximum;
char input[MAX * 10], *token;
float values_concatenation[4], sum1, sum2, value;
FILE *fp;

float rand_float(){
    return ((float) rand()) / ((float) RAND_MAX);
}

float random_float(const float a, const float b){
    if (a == b) return a;
    return rand_float() * (b - a) + a;
}

void handle_signal(int signal_num) {
    if(sig_value == 28){ // The 5th child
        if(read(p[0], input, sizeof(input)) == -1){ // Reading the values from the pipe
            perror("Couldn't read the parent's 4 values from the pipe!\n");
            exit(-8);
        } 

        token = strtok(input, ",");
        i = 0;

        while (token != NULL && i < 4) {
            values_concatenation[i++] = atof(token);
            token = strtok(NULL, ",");
        }

        sum1 = values_concatenation[0] + values_concatenation[1];
        sum2 = values_concatenation[2] + values_concatenation[3];
        sprintf(input, "%f,%f", sum1, sum2);

        if(write(p[1], input, sizeof(input)) == -1){ // Writing the new values to the pipe
            perror("Couldn't write the children's 2 values to the pipe!\n");
            exit(-9);
        } 

        kill(getppid(), sig_value); // Sending a signal to the parent to inform him that the work is done
    }
    else{ // The 4 players
        fp = fopen("range.txt", "r");

        if(fp == NULL){
            perror("Child couldn't open the file\n");
            exit(-10);
        }

        if (fscanf(fp, "%d,%d", &minimum, &maximum) != 2) {
            perror("Child couldn't read from the file!\n");
            fclose(fp);
            exit(-11);
        }

        fclose(fp);
        value = random_float(minimum, maximum); // Generating the random value
        sprintf(input, "%d.txt", my_pid);
        fp = fopen(input, "w");

        if(fp == NULL){
            perror("Child couldnt open the file!\n");
            fclose(fp);
            exit(-12);
        }

        fprintf(fp, "%f\n", value);
        fclose(fp);
        kill(getppid(), sig_value); // Sending a signal to the parent to inform him that the work is done
    }
}

int main(int argc, char **argv){
    if(argc == 4){
        p[0] = atoi(argv[2]);
        p[1] = atoi(argv[3]);
    }
    else if(argc != 2){
        perror("Usage: <int> for the players, <int, reading_pipe, writing_pipe> for the co-processor\n");
        exit(-15);
    }
    if(!check_numerical(argv[1])){
        perror("The signal passed must be an integer\n");
        exit(-22);
    }
    
    srand(time(NULL) ^ getpid());
    my_pid = getpid();
    sig_value = atoi(argv[1]);

    signal(SIGUSR1, handle_signal);

    while(1){
        pause();
    }

    return 0;
}