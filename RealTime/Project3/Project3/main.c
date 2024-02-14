#include "header.h"

FILE *fp;
int number_of_ants, min_speed, max_speed, eating_portion, food_placing_time, cnt, end_time;
float smell_distance, pheromone_min, pheromone_dist;
pthread_t *ants;
struct ANT *ant;
struct FOOD *food;
int sz;

void generate_ants_array(){
    ant = (struct ANT*) malloc(number_of_ants * sizeof(struct ANT));

    if(ant == NULL){
        fprintf(stderr, "Error creating the array of ants\n");
        exit(-1);
    }

    for(int i = 0; i < number_of_ants; i++){
        ant[i].curr_pheromone = -1;
        ant[i].toX = ant[i].toY = -1;
    }

    food = (struct FOOD*) malloc(1000 * sizeof(struct FOOD));

    if(food == NULL){
        fprintf(stderr, "Error creating the array of food\n");
        exit(-2);
    }
}

void get_arguments(char* file_name){
    fp = fopen(file_name, "r");

    if(fp == NULL){
        fprintf(stderr, "Error opening the file %s\n", file_name);
        exit(-2);
    }

    char * line = NULL;
    size_t len = 0;
    ssize_t read;
    int cnt = 0;

    while ((read = getline(&line, &len, fp)) != -1) {
        line[strcspn(line, "\n")] = '\0';
        char *split = strtok(line, "=");
        char **token = (char**) malloc(2 * sizeof(char*));

        if(token == NULL){
            fprintf(stderr, "Couldn't allocate for token\n");
            exit(-4);
        }

        int i = 0;
        while (split != NULL) {
            token[i++] = split;
            split = strtok(NULL, "=");
        }

        if(!strcmp(token[0], "number_of_ants") && check_numerical(token[1])){
            number_of_ants = atoi(token[1]); cnt++;
        }
        else if(!strcmp(token[0], "speed_min") && check_numerical(token[1])){
            min_speed = atoi(token[1]); cnt++;
        }
        else if(!strcmp(token[0], "speed_max") && check_numerical(token[1])){
            max_speed = atoi(token[1]); cnt++;
        }
        else if(!strcmp(token[0], "food_placing") && check_numerical(token[1])){
            food_placing_time = atoi(token[1]); cnt++;
        }
        else if(!strcmp(token[0], "smell_distance")){
            char *end;
            smell_distance = strtod(token[1], &end); cnt++;
        }
        else if(!strcmp(token[0], "pheromone_min")){
            char *end;
            pheromone_min = strtod(token[1], &end); cnt++;
        }
        else if(!strcmp(token[0], "eating_portion") && check_numerical(token[1])){
            eating_portion = atoi(token[1]);
            if(eating_portion != 0 && eating_portion <= 100){
                cnt++;
            }
        }
        else if(!strcmp(token[0], "end_time") && check_numerical(token[1])){
            end_time = atoi(token[1]); cnt++;
        }
        else if(!strcmp(token[0], "pheromone_dist")){
            char *end;
            pheromone_dist = strtod(token[1], &end); cnt++;
        }
    
        free(split);
        free(token);
    }

    if(cnt != 9){
        fprintf(stderr, "The arguments file %s is missing some data or doesn't follow the requierments\n", file_name);
        exit(-5);
    }

    if(fclose(fp) != 0){
        fprintf(stderr, "Error closing the file %s\n", file_name);
        exit(-3);
    }
}

void init(int idx){
    float speedX = random_range(min_speed, max_speed) / 1000.0;
    float speedY = random_range(min_speed, max_speed) / 1000.0;
    speedX += (speedX == 0.0f);
    speedY += (speedY == 0.0f);
    ant[idx].speedX = speedX;
    ant[idx].speedY = speedY;

    ant[idx].curr_pheromone = 1e9;

    ant[idx].toX = ant[idx].toY = -1;

    ant[idx].radius = 0.005f;
    ant[idx].circleX = random_range(-950, 950) / 1000.0f;
    ant[idx].circleY = random_range(-950, 950) / 1000.0f;

    ant[idx].toX = ant[idx].toY = ant[idx].toIdx = -2;
    ant[idx].dir = random_range(0, 8) * 45;
}

void *routine(void *arg){
    int idx = *(int*) arg;
    init(idx);
    while(1){
        if(cnt * 10 >= end_time * 1000){
            break;
        }

        for(int i = 0; i < number_of_ants; i++){
            if(i == idx) continue;
            float d = dist(ant[idx].circleX, ant[idx].circleY, ant[i].circleX, ant[i].circleY);

            if(ant[i].curr_pheromone > (ant[idx].curr_pheromone == 1e9 ? -1 : ant[idx].curr_pheromone) 
                && ant[i].curr_pheromone != 1e9 && d <= pheromone_dist && food[ant[i].toIdx].id != idx){   

                ant[idx].toX = ant[i].toX;
                ant[idx].toY = ant[i].toY;
                ant[idx].toIdx = ant[i].toIdx;
            }
        }

        if(ant[idx].toIdx == -2){
            int d = random_range(0, 1) * 2 - 1;
            int cnt2 = 0;
            while (hit(ant[idx].circleX + 0.003f * cosf(ant[idx].dir), ant[idx].circleY + 0.003 * sinf(ant[idx].dir))) {
                ant[idx].dir = (int) (ant[idx].dir + d * (135.0 * M_PI) / 180.0f) % 360;
                if(cnt2 == 1e8){
                    break;
                }
                cnt2++;
            }

            if(cnt2 == 1e7){
                init(idx);
                continue;
            }

            ant[idx].circleX += cosf(ant[idx].dir) * ant[idx].speedX;
            ant[idx].circleY += sinf(ant[idx].dir) * ant[idx].speedY;

            
            float best = 1e9;
            int idx_best = -1;
            for(int i = 0; i < sz; i++){
                if(food[i].portion == 0 || (number_of_ants > 1 && food[i].id == idx)) continue;

                float d = dist(ant[idx].circleX, ant[idx].circleY, food[i].fillingX, food[i].fillingY);
                if(d < best && d <= smell_distance){
                    best = d;
                    idx_best = i;
                }
            }

            if(idx_best != -1){
                ant[idx].toX = food[idx_best].fillingX;
                ant[idx].toY = food[idx_best].fillingY;
                ant[idx].toIdx = idx_best;
                ant[idx].curr_pheromone = 1.0f / dist(ant[idx].circleX, ant[idx].circleY, 
                                                        food[idx_best].fillingX, food[idx_best].fillingY);
            }
        }
        
        if(ant[idx].toIdx != -2){
            float best = dist(ant[idx].circleX, ant[idx].circleY, 
                food[ant[idx].toIdx].fillingX, food[ant[idx].toIdx].fillingY);

            if (best <= 0.1f && food[ant[idx].toIdx].portion > 0) {
                while(food[ant[idx].toIdx].portion > 0){
                    if(pthread_mutex_lock(&food[ant[idx].toIdx].lock) != 0){
                        fprintf(stderr, "Couldn't lock the mutex of food %d by ant %d\n", ant[idx].toIdx + 1, idx + 1);
                        continue;
                    }
                    ant[idx].dir = 0;
                    
                    food[ant[idx].toIdx].portion -= eating_portion;
                    food[ant[idx].toIdx].portion = max(food[ant[idx].toIdx].portion, 0);
                    food[ant[idx].toIdx].id = idx;

                    if(pthread_mutex_unlock(&food[ant[idx].toIdx].lock) != 0){
                        fprintf(stderr, "Couldn't unlock the mutex of food %d by ant %d\n", ant[idx].toIdx + 1, idx + 1);
                        continue;
                    }
                    
                    sleep(1);
                }
                ant[idx].toX = ant[idx].toY = -2;
                ant[idx].curr_pheromone = 1e9;
                ant[idx].toIdx = -2;
                ant[idx].dir = random_range(0, 8) * 45;
            } 
            else {
                if(food[ant[idx].toIdx].portion == 0){
                    ant[idx].toX = ant[idx].toY = -2;
                    ant[idx].curr_pheromone = 1e9;
                    ant[idx].toIdx = -2;
                    ant[idx].dir = random_range(0, 8) * 45;
                    continue;
                }

                ant[idx].curr_pheromone = 1.0f / (best + (best == 0.0f));

                float destx = food[ant[idx].toIdx].fillingX - ant[idx].circleX;
                float desty = food[ant[idx].toIdx].fillingY - ant[idx].circleY;
                float directionX = destx / (0.00001f + dist(destx, destx, desty, desty));
                float directionY = desty / (0.00001f + dist(destx, destx, desty, desty));

                ant[idx].circleX += directionX * ant[idx].speedX;
                ant[idx].circleY += directionY * ant[idx].speedY;
            }
        }
        usleep(10000);
    }

    free(arg);
}

void create_ants(){
    ants = malloc(sizeof(pthread_t) * number_of_ants);

    if(ants == NULL){
        fprintf(stderr, "Error creating the ants thread array\n");
        exit(-6);
    }

    for(int i = 0; i < number_of_ants; i++){
        int *a = malloc(sizeof(int));
        *a = i;
        if(pthread_create(&ants[i], NULL, &routine, a) != 0){
            fprintf(stderr, "Error creating a thread for ant %d\n", i + 1);
            exit(-7);
        }
    }
}

void addPizza(float x, float y) {
    if(pthread_mutex_init(&food[sz].lock, NULL) != 0){
        fprintf(stderr, "Couldn't init mutex for food %d\n", sz + 1);
        return;
    }

    food[sz].fillingX = food[sz].crustX = x;
    food[sz].fillingY = food[sz].crustY = y;
    food[sz].portion = 100;
    food[sz].id = -1;
    sz++;
}

void drawAnt(float x, float y, float radius, int dir){

    glColor3f(0.647059, 0.164706, 0.164706); 
    glBegin(GL_POLYGON);
    glVertex2f((x - 0.011), y);
    glVertex2f(x, y);
    glVertex2f(x, y + 0.025);
    glVertex2f((x - 0.011), y + 0.025);
    glEnd();

    glColor3f(0.647059, 0.164706, 0.164706);  
    glBegin(GL_LINES);

    glVertex2f((x), y);
    glVertex2f(x + 0.003, y - 0.01);

    glVertex2f((x-0.011), y);
    glVertex2f(x -0.013, y - 0.01);
    

    glVertex2f((x+0.011), y);
    glVertex2f(x , y + 0.01);
    
    glVertex2f(x -0.011, y+ 0.01);
    glVertex2f(x - 0.025, y );

    glVertex2f(x - 0.011, y + 0.0175);
    glVertex2f(x - 0.025, y +0.0175 );

    glVertex2f(x, y + 0.0175);
    glVertex2f(x + 0.0125, y + 0.0175 );


    glVertex2f((x+ 0.003), y +0.06);
    glVertex2f(x - 0.003, y + 0.03 );
    
    glVertex2f((x- 0.01), y +0.03);
    glVertex2f(x - 0.018, y + 0.06 );
    glEnd();

    int numSegments = 100;
    float angleIncrement = 2.0 * M_PI / numSegments;

    glBegin(GL_TRIANGLE_FAN);
    glColor3f(0.647059, 0.164706, 0.164706);

    float newX =x-0.005;
    float newY = y + 0.035; 
    glVertex2f(newX, newY);

    for (int i = 0; i <= numSegments; ++i) {
        float angle = i * angleIncrement;
        float xx = newX + radius * cos(angle);
        float yx = newY + radius * sin(angle);
        glVertex2f(xx, yx);
    }
    glEnd();

    glPopMatrix();
}

void pizza(int value){
    float x = random_range(-940, 940) / 1000.0f;
    float y = random_range(-940, 940) / 1000.0f;
    addPizza(x, y);
    glutPostRedisplay();
    glutTimerFunc(food_placing_time * 1000, pizza, 0);
}

void drawPizza(float x, float y, float z, float w, int portion){
    float fillingX = x;
    float fillingY = y;
    float crustX = z;
    float crustY = w;

    glColor4f(1.0, 1.0, 0.0, 1);

    float nowPortion2 = portion;

    glColor3f(0.8f, 0.4f, 0.1f);
    glBegin(GL_POLYGON);
    glVertex2f(crustX - 0.05f, crustY - 0.05f);
    glVertex2f(crustX - 0.05f, crustY + 0.05f);
    glVertex2f(crustX + 0.05f, crustY + 0.05f);
    glVertex2f(crustX + 0.05f, crustY - 0.05f);
    glEnd();
    
    glColor3f(1.0f, 0.0f, 0.0f); 

    int numToppings = 3;
    float radius = 0.01f;

    glColor3f(0.9f, 0.8f, 0.0f);
    glBegin(GL_TRIANGLE_FAN);
    glVertex2f(fillingX, fillingY);
    int numSlices = 100; 
    int portion2 = numSlices - nowPortion2; 

    for (int i = 0; i <= 100 - portion2; i++) {
        float angle = 2.0f * M_PI * i / numSlices;
        float x2 = fillingX + 0.05f * cos(angle);
        float y2 = fillingY + 0.05f * sin(angle);
        glVertex2f(x2, y2);
    }
    glEnd();

    float toppingX[3] = {0.02f, -0.02f, 0.0f};
    float toppingY[3] = {0.02f, 0.02f, -0.02f};
    float percentage = 2;

    if(nowPortion2 > 70){
        percentage = 3;
    }
    else if(nowPortion2 > 40){
        percentage = 2;
    }
    else if (nowPortion2 > 10){
        percentage = 1;
    }
    else {
        percentage = 0;
    }
    
    glColor3f(1.0f, 0.0f, 0.0f); 
    for (int i = 0; i < percentage; i++) {
        float x = fillingX + toppingX[i];
        float y = fillingY + toppingY[i];
        
        glBegin(GL_POLYGON);
        for (int j = 0; j <= 360; j += 5) {
            float theta = 2.0f * M_PI * j / 360.0;
            float vx = x + radius * cos(theta);
            float vy = y + radius * sin(theta);
            glVertex2f(vx, vy);
        }
        glEnd();
    }
}

void display(){
    cnt++;
    glClearColor(0.9, 0.91, 0.98, 1.0);   
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();
    glRotatef(90, 0.0, 0.0, 1.0); 


    for(int i = 0; i < sz; i++){
        if(food[i].portion == 0) continue;
        drawPizza(food[i].fillingX, food[i].fillingY, food[i].crustX, food[i].crustY, food[i].portion);
    }
    
    for(int i = 0; i < number_of_ants; i++){
        drawAnt(ant[i].circleX, ant[i].circleY, 0.0125f, ant[i].dir);
    }
    glRotatef(0, 0.0, 0.0, 1.0); 
    glFlush();
}

void update(int value){
    glutPostRedisplay();
    glutTimerFunc(10, update, 0);
}

void handle(int sig){
    for(int i = 0; i < sz; i++){
        pthread_mutex_destroy(&food[i].lock);
    }
    exit(0);
}

void end(){
    for(int i = 0; i < number_of_ants; i++){
        pthread_join(ants[i], NULL);
    }
    for(int i = 0; i < sz; i++){
        pthread_mutex_destroy(&food[i].lock);
    }
    exit(0);
}

void *openGLRoutine(){
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(1000, 1000);
    glutCreateWindow("Project3_1190041_1190102_1192301");
    glutDisplayFunc(display);
    glutTimerFunc(10, update, 0);
    glutTimerFunc(food_placing_time * 1000, pizza, 0);
    glutMainLoop();
}

int main(int argc, char **argv){
    if(argc != 2){
        fprintf(stderr, "Usage: <arguments file>\n");
        exit(-1);
    }

    glutInit(&argc, argv);

    signal(SIGINT, handle);
    srand(time(NULL));
    get_arguments(argv[1]);
    generate_ants_array();

    pthread_t openGL;
    pthread_create(&openGL, NULL, &openGLRoutine, NULL);

    create_ants();
    end(); 
    return 0;
}