#include "header.h"

pid_t parent, children[5];
int rounds = 5, cnt, i, ties = 0, round_number, mn, mx, signals_ids[5] = {12, 14, 2, 18, 28}, p[2], team[2] = {0, 0};
float round_values[4], sums[2];
char file_name[MAX], values_concatenation[MAX * 10], command[MAX * 10], *token, reading_pipe[100], writing_pipe[100], signal_id[5];
FILE* fp;

void generate_children(){
    for(i = 0; i < 5; i++){
        children[i] = fork();
        if(children[i] == -1){
            perror("No enough memory to create children!\n");
            exit(-1);
        }
        else if(children[i] == 0){ // Child
            sprintf(signal_id, "%d", signals_ids[i]);
            if(i == 4){
                sprintf(reading_pipe, "%d", p[0]);
                sprintf(writing_pipe, "%d", p[1]);
                execl("./child", "child", signal_id, reading_pipe, writing_pipe, NULL);
            }
            else{
                execl("./child", "child", signal_id, NULL);
            }
            break;
        }
        else{ // Parent
            printf("This is child #%d, the id is: %d\n", i + 1, children[i]);
        }
    }
    printf("\n");
}

void generate_min_max(char* fileName){
    fp = fopen(fileName, "w");
    if(fp == NULL){
        perror("Error opening the file!\n");
        exit(-2);
    }
    mn = (int) (MINIMUM + (rand() % (MAXIMUM - MINIMUM)));
    mx = (int) (mn + (rand() % (MAXIMUM - mn)));
    fprintf(fp, "%d,%d\n", mn, mx);
    fclose(fp);
    printf("\nThe parent generated the range: %d %d\n\n", mn, mx);
}

void signal_handler(int sig){ // Handler for the 1st child
    cnt++;
}

void signal_handler2(int sig){ // Handler for the 2nd child
    cnt++;
}

void signal_handler3(int sig){ // Handler for the 3rd child
    cnt++;
}

void signal_handler4(int sig){ // Handler for the 4th child
    cnt++;
}

void signal_handler5(int sig){ // Handler for the 5th child
    cnt++;
}

int main(int argc, char **argv){
    if(argc == 2){
        if(check_numerical(argv[1]) && atoi(argv[1]) > 0){
            rounds = atoi(argv[1]);
        }
        else{
            perror("The number of rounds must be an integer and positive\n");
            exit(-14);
        }
    }
    else if(argc != 1){
        perror("Usage: <# of rounds>\n");
        exit(-13);
    }
    
    srand(time(NULL) ^ getpid());

    if(pipe(p) < 0){ // Creating the pipe for the communication between the parent and the 5th child
        perror("Couldn't create the pipe\n");
        exit(-3);
    }

    // Defining a signal for each of the 4 players to communicate with the parent and the co-processor
    signal(SIGUSR2, signal_handler);
    signal(14, signal_handler2);
    signal(2, signal_handler3);
    signal(18, signal_handler4);
    signal(28, signal_handler5);

    parent = getpid();
    generate_children();

    for(round_number = 0; round_number < rounds; round_number++){
        printf("========================= Round #%d ==========================\n\n", round_number + 1);
        sleep(1);
        generate_min_max("range.txt");
        sleep(1);
        cnt = 0;

        for(i = 0; i < 4; i++){ // Sending signals to the 4 players to put their values to their files
            if(kill(children[i], SIGUSR1) < 0){
                perror("cou'dn't send the signals to the children\n");
                exit(-16);
            } 
        }

        while(cnt < 4) pause(); // Waiting for the responses of the 4 players

        for(i = 0; i < 4; i++){ // Getting the values of the players
            sprintf(file_name, "%d.txt", children[i]);
            fp = fopen(file_name, "r");

            if(fp == NULL){
                perror("Couldn't open the child's file!\n");
                exit(-23);
            }

            if (fscanf(fp, "%f", &round_values[i]) != 1) {
                perror("Parent couldnt read the child's value from the file!\n");
                fclose(fp);
                exit(-5);
            }

            fclose(fp);
            printf("Child #%d generated the value %.5f\n", i + 1, round_values[i]);
        }
        
        sprintf(values_concatenation, "%f,%f,%f,%f", round_values[0], round_values[1], round_values[2], round_values[3]);

        sprintf(command, "./animation %d %d %d %.5f %.5f %.5f %.5f %.5f %.5f", round_number + 1, team[0], team[1], 
            (float) mn / 100.0f, (float) mx / 100.0f, (float) round_values[0] / 100.0f, (float) round_values[1] / 100.0f, 
                (float) round_values[2] / 100.0f, (float) round_values[3] / 100.0f);
                
        system(command);

        cnt = 0;

        if(write(p[1], values_concatenation, sizeof(values_concatenation)) == -1){ // writing the values to the pipe
            perror("Couldn't write the children's 4 values to the pipe!\n");
            exit(-6);
        }

        if(kill(children[4], SIGUSR1) < 0){ // Telling the 5th child to proccess the values from the pipe
            perror("cou'dn't send the signals to the co-processor\n");
            exit(-17);
        } 

        while(cnt < 1) pause(); // Waiting for the 5th child to process the values

        if(read(p[0], values_concatenation, sizeof(values_concatenation)) == -1){ // Reading the proccesed values from the pipe
            perror("Couldn't read the co-processor's 2 values from the pipe!\n");
            exit(-7);
        } 

        token = strtok(values_concatenation, ",");
        i = 0;

        while (token != NULL && i < 2) {
            sums[i++] = atof(token);
            token = strtok(NULL, ",");
        }

        printf("\nTeam 1 scored %.5f\n", sums[0]);
        printf("Team 2 scored %.5f\n\n", sums[1]);

        if(sums[0] != sums[1]){
            if(sums[0] > sums[1]){
                team[0]++;
                printf("Team 1 won this round\n");
            }
            else{
                team[1]++;
                printf("Team 2 won this round\n");
            }
        }
        else{
            ties++;
            printf("This round ended a tie\n");
        }
        printf("=============================================================\n\n");
    }
    printf("\nTeam 1 won %d rounds\n", team[0]);
    printf("Team 2 won %d rounds\n", team[1]);

    if(team[0] == team[1]){
        printf("\nThe competition resulted in a tie\nThe final score was: %d %d\nThe number of tie rounds was: %d\n", team[0], team[1], ties);
    }
    else{
        printf("\n\nThe winner is Team %d\nThe final score was: %d %d\nThe number of tie rounds was: %d\n", (team[0] > team[1] ? 1 : 2), team[0], team[1], ties);
    }

    for(i = 0; i < 5; i++){
        if(kill(children[i], SIGKILL) < 0){
            perror("Couldn't terminate the children");
            exit(-17);
        }
    }

    if(close(p[0]) < 0){
        perror("Couldn't close the reading pipe\n");
        exit(-18);
    }
    if(close(p[1]) < 0){
        perror("Couldn't close the writing pipe\n");
        exit(-19);
    }
    
    for(i = 0; i < 4; i++){
        sprintf(file_name, "%d.txt", children[i]);

        if(remove(file_name) != 0){
            perror("Error removing the resources!\n");
            exit(-20);
        }
    }

    sprintf(file_name, "range.txt");
    
    if(remove(file_name) != 0){
        perror("Error removing the resources!\n");
        exit(-21);
    }

    return 0;
}