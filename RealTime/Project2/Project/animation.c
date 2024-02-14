#include "header.h"

key_t key;
int msgQ_ID;

int screenWidth = 980;
int screenHeight = 720;
int number_of_spies = 0;
int number_of_helpers = 0;
int number_of_encoders = 0;
int number_of_receiver = 0;
int number_of_swaps = 0;
int master_receive = 0;
double time_master = 0, time_receiver = 0;
int sent_spy = 0;
char *winner = "?";

float squareSize = 0.04f;
float squareSize2 = 0.2f;

float squareX = -0.04f / 2.0f;
float squareYTopToCenter = 0.95f;
float squareYCenterToBottom = 0.45f;
float squareYCenterToBottom2 = 0.0f;

float squareSpeed = 0.005f;
int numCyclesTopToCenter = 0;
int numCyclesCenterToBottom = 0;
int numCyclesFromSpytoMaster = 0;
int numCyclesFromSharedToSpy = 0;

float squareXCenterCenter2 = -0.04f / 2.0f;
float squareYCenterCenter2 = -0.04f / 2.0f;
float squareXCenterLeft2 = -0.67;

char buffer[32];



int maxCycles = 50;

bool animationActive = false;
bool animationActive2 = false;
bool animationActive3 = false;
bool animationActive4 = false;
bool animationActive5 = false;
bool animationActive6 = false;
bool animationActive7 = false;
bool animationActive8 = false;
bool animationActive9 = false;
bool animationActive10 = false;
bool animationActive11 = false;

void drawSquare(float x, float y, float size) {
    glBegin(GL_QUADS);
    glVertex2f(x, y);
    glVertex2f(x + size, y);
    glVertex2f(x + size, y + size);
    glVertex2f(x, y + size);
    glEnd();
}


float squareXCenterCenter = - 0.0f;
float squareYCenterCenter = -0.01f;
float squareXCenterLeft = -1.0f +  0.2f / 2.0f;
int numCycles4;
void animateFromSharedToSpy() {
    if (animationActive4) {
        squareXCenterCenter -= 0.008;

        if (squareXCenterCenter <= -0.68f) {
            numCycles4++;

            if (numCycles4 >= sent_spy) {
                animationActive4 = false;
            } else {
                squareXCenterCenter = -0.15f;
            }
        } 

    }
}



void animateSenderToHelper() {
    if (animationActive) {
        squareYTopToCenter -= squareSpeed;
                                //to where
        if (squareYTopToCenter <= 0.5f) {
            numCyclesTopToCenter++;

            if (numCyclesTopToCenter >= number_of_helpers) {
                animationActive = false;
            } else {
                                    //when restart restart from where
                squareYTopToCenter = 0.75f;
            }
        }
    }
}

int numCyclesSpyToMaster=0;
void animateSpyToMaster() {
    
    if (animationActive6) {
        squareYCenterToBottom2 -= 0.008;
 
        if (squareYCenterToBottom2 <= -0.5f ) {
            numCyclesSpyToMaster++;

            if (numCyclesSpyToMaster >= sent_spy) {
                animationActive6 = false;
               
            } else {
                squareYCenterToBottom2 = 0.0f; 
            }

        }
    }

}

void animateHelperToShared() {
    
    if (animationActive2) {
        squareYCenterToBottom -= 0.008;
 
        if (squareYCenterToBottom <= (-0.3f / 2.0f) +0.15f ) {
            numCyclesCenterToBottom++;

            if (numCyclesCenterToBottom >= number_of_swaps) {
                animationActive2 = false;
               
            } else {
                squareYCenterToBottom = 0.50f - 0.1f / 2.0f;
          
            
            }

        }
    }

}

// animate from shared to receiver
float squareXCenter = 0.0f;
float squareYCenter = -0.01f;
float squareXRight = 0.64f;
float squareYRight = 0.0f;
float squareXBottom = 0.0f;
float squareYBottom = -0.5;
bool moveRight = true;
bool moveBottom = false;
int repetitionCount = 0;
void animatefromSharedToReceiver() {
    if(animationActive7){

    if (moveRight) {
        squareXCenter += 0.008;

        if (squareXCenter >= squareXRight) {
            moveRight = false;
            moveBottom = true;
        }
    } else if (moveBottom) {
        squareYCenter -= 0.008;

        if (squareYCenter <= squareYBottom) {
            moveBottom = false;
            repetitionCount++;
            if (repetitionCount >= number_of_receiver) {
               animationActive7=false;
            }
            squareXCenter = 0.0f;
            squareYCenter = 0.0f;
            moveRight = true;
        }
    }
    }
}


// animate from master to parent 
float squareXCenter2 = -0.678f;
float squareYCenter2 = -0.52f;
float squareXRight2 = -0.02f;
float squareYRight2 = -0.5f;
float squareXBottom2 = 0.05f;
float squareYBottom2 = -0.85;
bool moveRight2 = true;
bool moveBottom2 = false;
int repetitionCount2=0;
void animatefromMasterToParent() {
    
    if(animationActive8){
    if (moveRight2) {
        squareXCenter2 += 0.008;
    
        if (squareXCenter2 >= squareXRight2) {
            moveRight2 = false;
            moveBottom2 = true;
        }
    } else if (moveBottom2) {
        squareYCenter2 -= 0.008;

        if (squareYCenter2 <= squareYBottom2) {
            moveBottom2 = false;
            repetitionCount2++;
            if (repetitionCount2 >= master_receive) {
               animationActive8=false;
            }
            squareXCenter2 = -0.67f;
            squareYCenter2 = -0.52f;
            moveRight2 = true;
        }
    }
    }
}




// animate from receiver to parent 
float squareXCenter3 = 0.67f;
float squareYCenter3 = -0.52f;
float squareXLeft3 = -0.02f;
float squareYLeft3 = -0.5f;
float squareXBottom3 = 0.05f;
float squareYBottom3 = -0.85;
bool moveLeft3 = true;
bool moveBottom3 = false;
int repetitionCount3=0;
void animatefromreceiverToParent() {
    
    if(animationActive9){
    if (moveLeft3) {
        squareXCenter3 -= 0.008;
    
        if (squareXCenter3 <= squareXLeft3) {
            moveLeft3 = false;
            moveBottom3 = true;
        }
    } else if (moveBottom3) {
        squareYCenter3 -= 0.008;

        if (squareYCenter3 <= squareYBottom3) {
            moveBottom3 = false;
            repetitionCount3++;
            if (repetitionCount3 >= number_of_receiver) {
               animationActive9=false;
            }
            squareXCenter3 = 0.6f;
            squareYCenter3 = -0.52f;
            moveLeft3 = true;
        }
    }
    }
}



// animate from receiver to shared 
float squareXCenter4 = 0.65f;
float squareYCenter4 = -0.52f;
float squareXUp4 = 0.0f;
float squareYUp4 = -0.015f;
float squareXLeft4 = 0.0f;
float squareYLeft4 = 0.0;
bool moveUp3 = true;
bool moveLeft4 = false;
int repetitionCount4=0;
void animatefromreceiverToShared() {
    
    if(animationActive10){
    if (moveUp3) {
        squareYCenter4 += 0.008;
    
        if (squareYCenter4 >= squareYUp4) {
            moveUp3 = false;
            moveLeft4 = true;
        }
    } else if (moveLeft4) {
        squareXCenter4 -= 0.008;

        if (squareXCenter4 <= squareYLeft4) {
            moveLeft4 = false;
            repetitionCount4++;
            if (repetitionCount4 >= number_of_receiver) {
               animationActive10=false;
            }
            squareXCenter4 = 0.65f;
            squareYCenter4 = -0.52f;
            moveUp3 = true;
        }
    }
    }
}


//animateFromSpyToMaster
int SpyToSharedCycles=0;
void animateFromSpyToShared() {
    if (animationActive5) {
        squareXCenterLeft2 += 0.008;

        if (squareXCenterLeft2 >= squareXCenterCenter2) {
            SpyToSharedCycles++;

            if (SpyToSharedCycles >= sent_spy) {
                animationActive5 = false;
            } else {
                squareXCenterLeft2 = -0.67;
            }
        }
    }
}

void display() {
    glClear(GL_COLOR_BUFFER_BIT);
    glLoadIdentity();


    glColor3f(0.5f, 0.5f, 0.5f);
    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(0.0f, 0.70f); 
    glVertex2f(0.0f, 0.0f); 
    glEnd();


//from shared mem to Reciever
    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(0.0f, 0.0f);  
    glVertex2f(0.67f, 0.0f);  
    glEnd();

    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(0.67f, 0.0f); 
    glVertex2f(0.67f, -0.5f); 
    glEnd();

//from shared mem to master spy
    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(0.0f, 0.0f);  
    glVertex2f(-0.67f, 0.0f); 
    glEnd();

    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(-0.65f, 0.0f); 
    glVertex2f(-0.65f, -0.5f);
    glEnd();

//from shared mem to master spy
    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(-0.65f, -0.5f);  
    glVertex2f(0.65f, -0.5f); 
    glEnd();
    glLineWidth(2.5f);
    glBegin(GL_LINES);
    glVertex2f(0.0f, -0.5f);  
    glVertex2f(0.0f, -0.75f);  
    glEnd();

    glColor3f(0.91,0.76,0.65);
    drawSquare(squareX, squareYTopToCenter, squareSize);

    glColor3f(0.184314,0.309804,0.309804);
    drawSquare(squareX, squareYCenterToBottom, squareSize);
    
    glColor3f(0.91,0.76,0.65);
    drawSquare(squareXCenterCenter, squareYCenterCenter, squareSize);
     
    glColor3f(0.3,0.3,0.7);
    drawSquare(squareXCenterLeft2, squareYCenterCenter2, squareSize);    
     
    glColor3f(0.3,0.3,0.7);
    drawSquare(-0.67, squareYCenterToBottom2, squareSize);    
    
    glColor3f(0.91,0.76,0.65);
    drawSquare(squareXCenter, squareYCenter, squareSize);
 
    glColor3f(0.3,0.3,0.7);
    drawSquare(squareXCenter2, squareYCenter2, squareSize);

    glColor3f(0.196078,0.8,0.6);
    drawSquare(squareXCenter3, squareYCenter3, squareSize);

    glColor3f(0.196078,0.8,0.6);
    drawSquare(squareXCenter4, squareYCenter4, squareSize);

//  Sender
    float size = 0.2f;  
    float x = -size/ 2.0f;  
    float y = 0.80f - size / 2.0f;  
    glColor3f(0.91,0.76,0.65);
    drawSquare(x, y, size);
    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(x+0.03, y + 0.08);
    glutBitmapString(GLUT_BITMAP_HELVETICA_18, (const unsigned char*) "SENDER");



// Helper   
    glColor3f(0.184314,0.309804,0.309804);
    size=0.15f;
    x = (-size / 2.0f) ;   
    y = 0.50f - size / 2.0f;     
    drawSquare(x, y, size);
    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(-0.06, 0.5);
    glutBitmapString(GLUT_BITMAP_HELVETICA_12, (const unsigned char*) "HELPER");


//Shared Memory  
    glColor3f(0.91,0.76,0.65);
    float width = 0.4f;
    float height = 0.3f;
    float x3 = -width / 2.0f;  
    float y3 = -height / 2.0f; 
    glBegin(GL_QUADS);
    glVertex2f(x3, y3);  
    glVertex2f(x3, y3 + height); 
    glVertex2f(x3 + width, y3 + height); 
    glVertex2f(x3 + width, y3); 
    glEnd();
    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(x3+0.04, y3 +0.13);
    glutBitmapString(GLUT_BITMAP_HELVETICA_18, (const unsigned char*) "SHARED MEMORY");

// Spy  
    glColor3f(0.3,0.3,0.7);
    size = 0.1f;
    x = -0.7f + size / 2.0f; 
    y = 0.0f; 
    glBegin(GL_QUADS);
    glVertex2f(x - size / 2.0f, y - size / 2.0f);  
    glVertex2f(x - size / 2.0f, y + size / 2.0f); 
    glVertex2f(x + size / 2.0f, y + size / 2.0f);  
    glVertex2f(x + size / 2.0f, y - size / 2.0f);  
    glEnd();

    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(x-0.04, y3 +0.13);
    glutBitmapString(GLUT_BITMAP_HELVETICA_18, (const unsigned char*) "SPY");

// Reciever
    glColor3f(0.196078,0.8,0.6);
    size = 0.22f;
    x = 0.55f + size / 2.0f; 
    y = -0.5f; 
    glBegin(GL_QUADS);
    glVertex2f(x - size / 2.0f, y - size / 2.0f);  
    glVertex2f(x - size / 2.0f, y + size / 2.0f); 
    glVertex2f(x + size / 2.0f, y + size / 2.0f); 
    glVertex2f(x + size / 2.0f, y - size / 2.0f);  
    glEnd();
    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(x-0.085, y - 0.01);
    glutBitmapString(GLUT_BITMAP_HELVETICA_18, (const unsigned char*) "RECEIVER");

// Master Spy  
    glColor3f(0.35,0.35,0.67);
    size = 0.2f;
    x = -0.75f + size / 2.0f; 
    y = -0.5f; 
    glBegin(GL_QUADS);
    glVertex2f(x - size / 2.0f, y - size / 2.0f);  
    glVertex2f(x - size / 2.0f, y + size / 2.0f);  
    glVertex2f(x + size / 2.0f, y + size / 2.0f); 
    glVertex2f(x + size / 2.0f, y - size / 2.0f);  
    glEnd();
    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(x-0.07, y + 0.01 );
    glutBitmapString(GLUT_BITMAP_HELVETICA_18, (const unsigned char*) "MASTER\n   SPY");

//Parent  
    glColor3f(0.91,0.76,0.65);
    size=0.2f;
    x = -size / 2.0f;  
    y = -1.0f + size / 2.0f; 
    glBegin(GL_QUADS);
    glVertex2f(x, y);  
    glVertex2f(x, y + size); 
    glVertex2f(x + size, y + size);
    glVertex2f(x + size, y);
    glEnd();
    glColor4f(1.0f, 1.0f, 1.0f, 0.0f);
    glRasterPos2f(x+0.03, y + 0.08);
    glutBitmapString(GLUT_BITMAP_HELVETICA_18, (const unsigned char*) "PARENT");

//animate functions
    animatefromreceiverToShared();
    animatefromreceiverToParent();
    animatefromMasterToParent();
    animatefromSharedToReceiver();
    animateSenderToHelper();
    animateHelperToShared();
    animateFromSharedToSpy();
    animateFromSpyToShared();
    animateSpyToMaster();
 

// Texts 
    glColor3f(0.5, 0.5, 0.5); 

    glRasterPos2f(-0.95, 0.9);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "# Of Helpers: ");
    sprintf(buffer, "%d", number_of_helpers) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.8);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "# Of Encoders: ");
    sprintf(buffer, "%d", number_of_encoders) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.7);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "# Of Spies: ");
    sprintf(buffer, "%d", number_of_spies) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.6);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "# Of Helpers Swap: ");
    sprintf(buffer, "%d", number_of_swaps) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.5);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "# of Spy messages sent: ");
    sprintf(buffer, "%d", sent_spy) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.4);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "# Recieved by Receiver: ");
    sprintf(buffer, "%d", number_of_receiver) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.3);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Master finished in: ");
    sprintf(buffer, "%.5f", time_master) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.2);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Reciever finished in: ");
    sprintf(buffer, "%.5f", time_receiver) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.95, 0.1);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "The Winner is: ");
    sprintf(buffer, "%s", winner) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glutSwapBuffers();
}

void reshape(int width, int height) {
    screenWidth = width;
    screenHeight = height;
    glViewport(0, 0, screenWidth, screenHeight);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1.0, 1.0, -1.0, 1.0);
    glMatrixMode(GL_MODELVIEW);
}

void timer(int value) {
    struct msqid_ds msq_stat;
    if (msgctl(msgQ_ID, IPC_STAT, &msq_stat) == -1) {
        exit(-2);
    }
    if(msq_stat.msg_qnum > 0){    
        struct msgQ_animation message;

        if(msgrcv(msgQ_ID, &message, sizeof(message), 1, 0) == -1){
            fprintf(stderr, "Couldn't receive message\n");
            exit(-61);
        }

        char k[M];
        strcpy(k, message.d.key);
        if(strcmp(k, "spies") == 0){
            number_of_spies = message.d.value;
        }
        else if(strcmp(k, "helpers") == 0){
            number_of_helpers = message.d.value;
            animationActive = true;
        }
        else if(strcmp(k, "encoders") == 0){
            number_of_encoders = message.d.value;
        }
        else if(strcmp(k, "receiver") == 0){
            number_of_receiver++;
            animationActive7 = animationActive9 = animationActive10 = true;
        }
        else if(strcmp(k, "swap") == 0){
            number_of_swaps++;
            animationActive2 = true;
        }
        else if(strcmp(k, "time_receiver") == 0){
            time_receiver = message.d.time;
        }
        else if(strcmp(k, "time_master") == 0){
            time_master = message.d.time;
        }
        else if(strcmp(k, "spy_sent") == 0){
            sent_spy++;
            animationActive4 = true;
        }
        else if(strcmp(k, "master_spy_rec") == 0){
            master_receive++;
            animationActive8 = true;
        }
        else if(strcmp(k, "terminate") == 0){
            exit(0);
        }
        else{
            winner = (char*) malloc(32 * sizeof(char));
            strcpy(winner, message.d.key);
        }
    }
    glutPostRedisplay();
    glutTimerFunc(2, timer, 0);
    
}

int main(int argc, char** argv) {
    if(argc != 1){
        fprintf(stderr, "2 arguments only\n");
        exit(-1);
    }
    msgQ_ID = msgget(ID, 0666 | IPC_CREAT);

    if(msgQ_ID == -1){
        fprintf(stderr, "Couldn't msgget the key\n");
        exit(-72);
    }

    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_DOUBLE | GLUT_RGB);
    glutInitWindowSize(screenWidth, screenHeight);
    glutCreateWindow("Project#2_1190041_1190102_1192301");
    glClearColor(0.9, 0.91, 0.98, 1.0);
    glutDisplayFunc(display);
    glutReshapeFunc(reshape);
    glutTimerFunc(0, timer, 0);
    glutMainLoop();
    return 0;
}

