#include "header.h"

pid_t sender, receiver, master_spy, pid, *helpers_array, *spy_array, animator;
int helpers, spy, shmid, cnt, cnt2, max_column, rows, i, threshold;
char *line, *file_name1, *file_name2, *file_name3, *file_name4, *arguments, *shmptr;
FILE *fp;
size_t len;
ssize_t reading;
struct MEMORY *memory;
clock_t start;
double time_of_receiver, time_of_master;
key_t key;
int msgQ_ID;

void generate_sender(){
    sender = fork();

    if(sender < 0){
        fprintf(stderr, "Couldn't generate the sender\n");
        exit(-1);
    }
    else if(sender == 0){
        char *files[6];
        files[0] = "sender";
        files[1] = file_name1;
        files[2] = file_name2;
        files[3] = file_name3;
        files[4] = file_name4;
        files[5] = NULL;
        execvp("./sender", files);
        fprintf(stderr, "Shouldnt be here\n");
        exit(-2);
    }
    else{
        printf("\nGenerated the sender successfully, it's pid is: %d\n", sender);
    }
}

void generate_receiver(){
    receiver = fork();

    if(receiver < 0){
        fprintf(stderr, "Couldn't generate the receiver\n");
        exit(-3);
    }
    else if(receiver == 0){
        execl("./receiver", "receiver", NULL);
        fprintf(stderr, "Shouldnt be here\n");
        exit(-4);
    }
    else{
        printf("\nGenerated the receiver successfully, it's pid is: %d\n", receiver);
    }
}

void generate_master_spy(){
    master_spy = fork();

    if(master_spy < 0){
        fprintf(stderr, "Couldn't generate the master_spy\n");
        exit(-5);
    }
    else if(master_spy == 0){
        execl("./master_spy", "master_spy", "mq", "11", NULL);
        fprintf(stderr, "Shouldnt be here\n");
        exit(-6);
    }
    else{
        printf("\nGenerated the master_spy successfully, it's pid is: %d\n", master_spy);
    }
}

void generate_helpers(){
    helpers_array = (pid_t*) malloc(helpers * sizeof(pid_t));

    if(helpers_array == NULL){
        fprintf(stderr, "Couldn't generate the helpers array\n");
        exit(-7);
    }

    for(i = 0; i < helpers; i++){
        sleep(1);
        helpers_array[i] = fork();

        if(helpers_array[i] < 0){
            fprintf(stderr, "Couldn't generate helper %d\n", i + 1);
            exit(-8);
        }
        else if(helpers_array[i] == 0){
            execl("./helpers", "helpers", NULL);
            fprintf(stderr, "Shouldnt be here\n");
            exit(-9);
        }
        else{
            printf("\nGenerated helper %d successfully, it's pid is: %d\n", i + 1, helpers_array[i]);
        }
    }
}

void generate_spy(){
    spy_array = (pid_t*) malloc(spy * sizeof(pid_t));

    if(spy_array == NULL){
        fprintf(stderr, "Couldn't generate the spies array\n");
        exit(-10);
    }

    for(i = 0; i < spy; i++){
        sleep(1);
        spy_array[i] = fork();

        if(spy_array[i] < 0){
            fprintf(stderr, "Couldn't generate spy %d\n", i + 1);
            exit(-11);
        }
        else if(spy_array[i] == 0){
            execl("./spy", "spy", "mq", "11", NULL);
            fprintf(stderr, "Shouldnt be here\n");
            exit(-12);
        }
        else{
            printf("\nGenerated spy %d successfully, it's pid is: %d\n", i + 1, spy_array[i]);
        }
    }
}

void generate_shared_memory(){
    fp = fopen("max.txt", "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the file max.txt\n");
        exit(-13);
    }

    if(fscanf(fp, "%d %d", &max_column, &rows) != 2){
        fprintf(stderr, "Couldn't read the values from max.txt\n");
        exit(-14);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file max.txt\n");
        exit(-15);
    }

    shmid = shmget(getpid(), sizeof(struct MEMORY) * max_column, 0666 | IPC_CREAT);

    if(shmid < 0){
        fprintf(stderr, "Couldn't generate the ID of the shared memory\n");
        exit(-16);
    }

    memory = (struct MEMORY*) shmat(shmid, NULL, 0);

    if(memory == (void *) -1){
        fprintf(stderr, "Couldn't attach memory\n");
        exit(-17);
    }

    for(i = 0; i < max_column; i++){
        memory[i].done = 0;
    }
}

double getCurrentTime() {
	return (double) (clock() - start) / CLOCKS_PER_SEC;
}

void receiver_signal(int sig){
    time_of_receiver = getCurrentTime();
    cnt++;
}

void master_spy_signal(int sig){
    time_of_master = getCurrentTime();
    cnt2++;
}

void remove_shared_memory(){
    if(shmdt(memory) < 0){
        fprintf(stderr, "Couldn't detach memory\n");
        exit(-18);
    }
}

void get_arguments(){
    fp = fopen(arguments, "r");

    if(fp == NULL){
        fprintf(stderr, "Couldn't open the arguments file\n");
        exit(-19);
    }
    int cnt = 0;
    while(((reading = getline(&line, &len, fp))) != -1){
        line[strcspn(line, "\n")] = '\0';
        char *split = strtok(line, "=");
        char **token = (char**) malloc(2 * sizeof(char*));

        if(token == NULL){
            fprintf(stderr, "Couldn't allocate for token\n");
            exit(-58);
        }

        int i = 0;
        while (split != NULL) {
            token[i++] = split;
            split = strtok(NULL, "=");
        }
        
        if(strcmp(token[0], "file1") == 0){
            file_name1 = token[1];
            cnt++;
        }
        else if(strcmp(token[0], "file2") == 0){
            cnt++;
            file_name2 = token[1];
        }
        else if(strcmp(token[0], "file3") == 0){
            cnt++;
            file_name3 = token[1];
        }
        else if(strcmp(token[0], "file4") == 0){
            cnt++;
            file_name4 = token[1];
        }
        else if(strcmp(token[0], "helpers") == 0 && check_numerical(token[1])){
            cnt++;
            helpers = atoi(token[1]);
        }
        else if(strcmp(token[0], "spy") == 0 && check_numerical(token[1])){
            cnt++;
            spy = atoi(token[1]);
        }
        else if(strcmp("threshold", token[0]) == 0 && check_numerical(token[1])){
            cnt++;
            threshold = atoi(token[1]);
        }
        else{
            fprintf(stderr, "Arguments file not correct\n");
            exit(-1);
        }
        len = 0;
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Couldn't close the file %s\n", arguments);
        exit(-20);
    }

    if(cnt != 7 || threshold <= 0 || spy <= 0 || helpers <= 0){
        fprintf(stderr, "Arguments file not correct\n");
        exit(-1);
    }
}

void initiate_semaphore(){
    semaphore = sem_open("/read_write_semaphore", O_CREAT, 0777, 1);

    if (semaphore == SEM_FAILED) {
        fprintf(stderr, "Parent couldn't open the semaphore\n");
        sem_close(semaphore);
        sem_unlink("/read_write_semaphore");
        exit(-21);
    }
    
    if(sem_close(semaphore) < 0){
        fprintf(stderr, "Parent couldn't close the semaphore\n");
        sem_unlink("/read_write_semaphore");
        exit(-22);
    }
}

void process_sender(){
    cnt = 0;
    generate_sender();
    while(cnt < 1) pause();
    cnt = 0;
    if(kill(sender, SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the sender\n");
        exit(-23);
    }
    while(cnt < 1) pause();
}

void process_master_spy(){
    cnt = 0;
    generate_master_spy();
    while(cnt < 1) pause();
}

void process_receiver(){
    cnt = 0;
    generate_receiver();
    while(cnt < 1) pause();
}

void generate_helpers_and_spies(){
    generate_helpers();
    generate_spy();
}

int compareFiles(char* file1, char* file2) {
    FILE *fp1, *fp2;
    int ch1, ch2;

    fp1 = fopen(file1, "r");
    if (fp1 == NULL) {
        return 0;
    }

    fp2 = fopen(file2, "r");
    if (fp2 == NULL) {
        return 0;
    }

    ch1 = getc(fp1);
    ch2 = getc(fp2);

    while (ch1 != EOF && ch2 != EOF && ch1 == ch2) {
        ch1 = getc(fp1);
        ch2 = getc(fp2);
    }

    fclose(fp1);
    fclose(fp2);

    return ch1 == ch2;
}

void start_competition(){
    cnt = 0;
    if(kill(sender, SIGUSR2) < 0){
        fprintf(stderr, "Couldn't send a signal to the sender\n");
        exit(-24);
    }
    while(cnt < 1) pause();

    for(int i = 0; i < helpers; i++){
        if(kill(helpers_array[i], SIGUSR1) < 0){
            fprintf(stderr, "Couldn't send a signal to the helper %d\n", i + 1);
            exit(-25);
        }  
    } 

    for(int i = 0; i < spy; i++){
        if(kill(spy_array[i], SIGUSR1) < 0){
            fprintf(stderr, "Couldn't send a signal to the spy %d\n", i + 1);
            exit(-26);
        }  
    } 

    cnt = 0, cnt2 = 0;
    if(kill(receiver, SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the receiver\n");
        exit(-27);
    }
    
    if(kill(master_spy, SIGUSR1) < 0){
        fprintf(stderr, "Couldn't send a signal to the receiver\n");
        exit(-28);
    }

    while (cnt < 1 || cnt2 < 1) {
        if(getCurrentTime() - start > 30.0) break;
    }
    for(int i = 0; i < helpers; i++){
        if(kill(helpers_array[i], SIGKILL) < 0){
            fprintf(stderr, "Couldn't send a signal to the helper %d\n", i + 1);
        }  
    } 

    for(int i = 0; i < spy; i++){
        if(kill(spy_array[i], SIGKILL) < 0){
            fprintf(stderr, "Couldn't send a signal to the spy %d\n", i + 1);
        }  
    } 

    if(kill(receiver, SIGKILL) < 0){
        fprintf(stderr, "Couldn't send a signal to the receiver");
    }
    if(kill(master_spy, SIGKILL) < 0){
        fprintf(stderr, "Couldn't send a signal to the master spy");
    }
    if(kill(sender, SIGPOLL) < 0){
        fprintf(stderr, "Couldn't send a signal to the sender");
    }

    int count = 0;
    pid_t exited_pid;
    int status;
    while (count < 1) {
        exited_pid = waitpid(-1, &status, 0);
        if (WIFEXITED(status)) {
            count++;
        }
    }
}

void generate_animation(){
    animator = fork();

    if(animator < 0){
        fprintf(stderr, "Couldn't generate the master_spy\n");
        exit(-5);
    }
    else if(animator == 0){
        execl("./animation", "animation", NULL);
        fprintf(stderr, "Shouldnt be here\n");
        exit(-6);
    }
    else{
        printf("Generated the animator successfully, it's pid is: %d\n", animator);
    }
}

void send(){
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "spies");
    d.value = spy;
    message.d = d;
    message.type = 1;
    if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
        fprintf(stderr, "Couldn't send to animator\n");
    }
    sleep(2);

    strcpy(d.key, "helpers");
    d.value = helpers;
    message.d = d;
    message.type = 1;
    if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
        fprintf(stderr, "Couldn't send to animator\n");
    }
    sleep(2);
}

void send_time_receiver(){
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "time_receiver");
    d.time = time_of_receiver * 1000.0;
    message.d = d;
    message.type = 1;
    if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
        fprintf(stderr, "Couldn't send to animator\n");
    }
}

void send_time_master(){
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, "time_master");
    d.time = time_of_master * 1000.0;
    message.d = d;
    message.type = 1;
    if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
        fprintf(stderr, "Couldn't send to animator\n");
    }
}

void do_work(){
    generate_animation();
    pid = getpid();

    if(signal(SIGUSR1, receiver_signal) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function receiver_signal to a signal\n");
        exit(-31);
    }

    if(signal(SIGUSR2, master_spy_signal) == SIG_ERR){
        fprintf(stderr, "Couldn't attach the function master_spy_signal to a signal\n");
        exit(-32);
    }

    get_arguments();
    send();
    initiate_semaphore();
    process_sender();
    process_master_spy();
    generate_shared_memory();
    process_receiver();
    generate_helpers_and_spies();
    start_competition();
    remove_shared_memory();

    if(sem_unlink("/read_write_semaphore") < 0){
        fprintf(stderr, "Couldn't unlink semaphore\n");
        exit(-33);
    }
}

void send_winner(char *s){
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, s);
    message.d = d;
    message.type = 1;
    if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
        fprintf(stderr, "Couldn't send to animator\n");
    }
}

void send_done(char *s){
    struct msgQ_animation message;
    struct data d;
    strcpy(d.key, s);
    message.d = d;
    message.type = 1;
    if(msgsnd(msgQ_ID, &message, sizeof(message), 0) == -1){
        fprintf(stderr, "Couldn't send to animator\n");
    }
}

int main(int argc, char **argv){
    if(argc != 2){
        fprintf(stderr, "Pass the arguments file only\n");
        exit(-34);
    }
    
    start = time_of_master = time_of_receiver = getCurrentTime();
    arguments = argv[1];
    if(file_exists("spy.txt")) {
        if(remove("spy.txt") == -1){
            fprintf(stderr, "Couldn't remove existing receiver.txt\n");
            exit(-62);
        }
    }

    if(file_exists("receiver.txt")) {
        if(remove("receiver.txt") == -1){
            fprintf(stderr, "Couldn't remove existing receiver.txt\n");
            exit(-62);
        }
    }

    msgQ_ID = msgget(ID, 0666 | IPC_CREAT);

    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    do_work();

    int master_spy_correct = compareFiles(file_name3, "spy.txt");
    int receiver_correct = compareFiles(file_name3, "receiver.txt");

    if(receiver_correct) {
        printf("\n\nReceiver finished after %.5lf seconds from the start\n", 1000.0 * time_of_receiver);
        send_time_receiver();
    }
    if(master_spy_correct) {
        printf("Master finished after %.5lf seconds from the start\n", 1000.0 * time_of_master);
        send_time_master();
    }

    if(master_spy_correct ^ receiver_correct){
        if(master_spy_correct){
            printf("Master spy won\n");
            send_winner("Master Spy");
        }
        else{
            printf("Receiver won\n");
            send_winner("Receiver");
        }
    }
    else{
        if(master_spy_correct + receiver_correct == 0){
            printf("No one collected the columns\n");
            send_winner("No one collected the columns");
        }
        else if(time_of_master < time_of_receiver){
            printf("Master spy won\n");
            send_winner("Master Spy");
        }
        else if(time_of_master > time_of_receiver){
            printf("Receiver won\n");
            send_winner("Receiver");
        }
        else{
            printf("Its a tie\n");
            send_winner("Tie");
        }
    }

    sleep(25);
    send_done("terminate");
    return 0;
}