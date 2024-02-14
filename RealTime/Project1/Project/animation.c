#include "header.h"


#define screenWidth 800
#define screenHeight 1000

#define rectWidth 0.085
#define rectHeight 0.85

//Declaration of variables with initiallizing
int roundNumber = 0 ;
int firstTeamScore = 0;
int secondTeamScore = 0;
int tiesNumber = 0;
float stopGreen =  0.6;
float stopRed = -0.15;
float minRange = 0;
float maxRange = 0.85;
float firstPlayerValue = 0.0;
float secondPlayerValue  =0.0;
float thirdPlayerValue  = 0.0;
float fourthPlayerValue = 0.0;
char firstPlayerValueText[200]  = "First Player Value = ";
char secondPlayerValueText[200]  = "Second Player Value = ";
char thirdPlayerValueText[200]  = "Third Player Value = ";
char fourthPlayerValueText[200]  = "Fourth Player Value = ";
float redX = -0.82;     // initial x cordinate position for red square 
float greenX = -0.82;   // initial x cordinate position for green square 

void display() {
    glClear(GL_COLOR_BUFFER_BIT);

    char buffer[32];

    glColor3f(1, 0, 1); 
    // Add "Round# " text
    glRasterPos2f(-0.1, 0.85);// update the position
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Round# ");
    sprintf(buffer, "%d", roundNumber) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glColor3f(0.5, 0.5, 0.5); // set color to gray

    // Add "First Player" text
    glRasterPos2f(-0.85, 0.75);// update the position
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "First Team: ");

    // Add "Second Player" text
    glRasterPos2f(-0.85, 0.3);// update the position
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Second Team: ");

    // First rectangle
    glBegin(GL_POLYGON);
        glVertex2f(-0.85, 0.7);   // top-left vertex
        glVertex2f(-0.85, 0.4);   // bottom-left vertex
        glVertex2f(0.85, 0.4);    // bottom-right vertex
        glVertex2f(0.85, 0.7);    // top-right vertex
    glEnd();
    
    // Add a green square inside the first rectangle
    glColor3f(0.0, 1.0, 0.0); // set color to green
    glBegin(GL_POLYGON);
        glVertex2f(greenX, 0.6);   
        glVertex2f(greenX, 0.5);   
        glVertex2f(greenX + rectWidth, 0.5);  
        glVertex2f(greenX + rectWidth, 0.6);   
    glEnd();

    
    // Second rectangle
    glColor3f(0.5, 0.5, 0.5); // set color to gray
    glBegin(GL_POLYGON);
        glVertex2f(-0.85, 0.25);    
        glVertex2f(-0.85, -0.05);  
        glVertex2f(0.85, -0.05);    
        glVertex2f(0.85, 0.25);   
    glEnd();

    // Add a red square inside the second rectangle
    glColor3f(1.0, 0.0, 0.0); // set color to red
    glBegin(GL_POLYGON);
        glVertex2f(redX, 0.15);              
        glVertex2f(redX, 0.05);             
        glVertex2f(redX + rectWidth, 0.05);   
        glVertex2f(redX + rectWidth, 0.15);   
    glEnd();


    glColor3f(0.5, 0.5, 0.5); // set color to gray
    // Add "First Player Value: " text
    glRasterPos2f(-0.85, -0.3);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "First Player Value: ");
    sprintf(buffer, "%0.5f", firstPlayerValue * 100) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }
    
    // Add "Second Player Value: " text
    glRasterPos2f(-0.15, -0.3); // update the position
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Second Player Value: ");
    sprintf(buffer, "%0.5f", secondPlayerValue * 100);
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    // Add "Third Player Value: " text
    glRasterPos2f(-0.85, -0.4);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Third Player Value: ");
    sprintf(buffer, "%0.5f", thirdPlayerValue * 100);
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    // Add "Fourth Player Value: " text
    glRasterPos2f(-0.15, -0.4);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Fourth Player Value: ");
    sprintf(buffer, "%0.5f", fourthPlayerValue * 100);
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    // Add "Sum = " text
    glRasterPos2f(0.7, -0.3);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Sum = ");
    sprintf(buffer, "%0.5f", (firstPlayerValue + secondPlayerValue)*100 );
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(0.48, -0.3);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "------>");
    

    glRasterPos2f(0.48, -0.4);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "------>");
    

    glRasterPos2f(0.7, -0.4);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Sum = ");
    sprintf(buffer, "%0.5f", (thirdPlayerValue + fourthPlayerValue)*100 );
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }
 

    // Add "Minimum Range: " text
    glRasterPos2f(-0.85, -0.5);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Minimum Range: ");
    sprintf(buffer, "%.0f", minRange*100) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

 // Add "Minimum Range: " text
    glRasterPos2f(-0.85, -0.6);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Maximum Range: ");
    sprintf(buffer, "%.0f", maxRange*100) ;
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }


 
    glColor3f(1, 0, 1); // set color to purple 
    glRasterPos2f(-0.85, -0.75);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "The score: First Team: ");
    sprintf(buffer, "%d", firstTeamScore);
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }

    glRasterPos2f(-0.25, -0.75);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Second Team: ");
    sprintf(buffer, "%d", secondTeamScore);
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }


    glRasterPos2f(0.25, -0.75);
    glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "Tied In: ");
    sprintf(buffer, "%d", (tiesNumber));
    for (int i = 0; buffer[i] != '\0'; ++i) {
        glutBitmapCharacter(GLUT_BITMAP_TIMES_ROMAN_24, buffer[i]);
    }
 

    glColor3f(1, 0, 1); // set color to purple

    // Move squares to the right until they reach the stop position
    if (redX <= stopRed - rectWidth) {
        redX += 0.001;
    }
    if (greenX <= stopGreen - rectWidth) {
        greenX += 0.001;
    }

    glFlush();
    usleep(1000); // add a small delay to the animation
    glutPostRedisplay(); // call the display function again
}

void init() {
    glClearColor(1.0, 1.0, 1.0, 0.0);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluOrtho2D(-1.0, 1.0, -1.0, 1.0);
}

int main(int argc, char** argv) {

    //argv
    roundNumber =  atoi(argv[1]);
    firstTeamScore = atoi(argv[2]);
    secondTeamScore = atoi(argv[3]);
    minRange =  atof(argv[4]);
    maxRange = atof(argv[5]);
    firstPlayerValue = atof(argv[6]);
    secondPlayerValue =  atof(argv[7]);
    thirdPlayerValue = atof(argv[8]);
    fourthPlayerValue = atof(argv[9]);
  
    //if(maxRange == minRange) then its tied
    if(maxRange == minRange){
         stopGreen =  0.85; // x position to stop at
         stopRed = 0.85; 

    }else{
         stopGreen = (((firstPlayerValue + secondPlayerValue )/ 2.0f - minRange ) / (maxRange - minRange) )  * (0.85f + 0.85f) -0.85f ;
         stopRed = (((thirdPlayerValue + fourthPlayerValue)/ 2.0f - minRange ) / (maxRange - minRange) )  * (0.85f + 0.85f) -0.85f ;
    }

    glutInit(&argc, argv);
    glutInitDisplayMode(GLUT_SINGLE | GLUT_RGB);
    glutInitWindowSize(screenHeight, screenWidth);
    glutInitWindowPosition(100, 100);
    glutCreateWindow("Project#1_1190041_1190102_1192301");
    init();
    glutDisplayFunc(display);


    if (firstPlayerValue+secondPlayerValue > thirdPlayerValue + fourthPlayerValue){   
        glRasterPos2f(screenWidth/2000 - 0.35, -0.6);
        glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "<<< The Winner is the First Team >>>");
        firstTeamScore++;
    }else if(firstPlayerValue+secondPlayerValue < thirdPlayerValue + fourthPlayerValue){
        glRasterPos2f(screenWidth/2000 - 0.35, -0.6);
        glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "<<< The Winner is the Second Team >>>");
        secondTeamScore++;
    }else{
        glRasterPos2f(screenWidth/2000 - 0.1, -0.6);
        glutBitmapString(GLUT_BITMAP_TIMES_ROMAN_24, (const unsigned char*) "<<< Tied >>>");
        tiesNumber++;
    }
    glutMainLoop();
    return 0;
}