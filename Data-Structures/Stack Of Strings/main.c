#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#define nax 100

void infoMenu();
void menu();

// The struct we're using to store data, it is displayed as a stack.
struct Stack{
    char line[nax];
    struct Stack* next;
};


// Two stacks, "has" saves the input and "popped" saves the popped lines from "has".
struct Stack* has;
struct Stack* popped;

// The function that creates a stack by allocating a space for it in the memory.
struct Stack* createStack(){
    struct Stack* stack = (struct Stack*) malloc(sizeof(struct Stack));
    if(stack == NULL){
        printf("Out of memory!\n");
        return NULL;
    }
    stack -> next = NULL;
    return stack;
}

// This function adds a string at the top of the stack.
void push(struct Stack* stack, char s[nax]){
    struct Stack* st = (struct Stack*) malloc(sizeof(struct Stack));
    if(st == NULL){
        printf("Out of memory!\n");
        return;
    }
    strcpy(st -> line, s);
    st -> next = stack -> next;
    stack -> next = st;
}

// This functions returns 1 if the stack is empty and 0 otherwise.
int isEmpty(struct Stack* stack){
    return stack -> next == NULL;
}

// This function removes the string at the top of a stack and adds it to the other one.
// In case the user uses the "undo" command, it removes the top most string in "has" stack and adds it the the top of "popped" stack.
// In case the user uses the "redo" command, it removes the top most string in "popped" stack and adds it the the top of "has" stack.
void pop (struct Stack* stack1, struct Stack* stack2){
    if(isEmpty(stack1)){
        printf("Stack is either empty or full!\n");
        return;
    }
    struct Stack* st = stack1 -> next;
    push(stack2, stack1 -> next -> line);
    stack1 -> next = stack1 -> next -> next;
    free(st);
}

// This function clears the content of the current screen.
void clear_screan(){
    system("cls");
}

// This function prints the contents of the stack, but because the traversal prints the data reversed, we stored
// the data in a new stack so they get reversed and printed them correctly from the new stack.
void print(struct Stack* stack){
    if(isEmpty(stack)){
        printf("\nresult >\n");
        printf("Empty stack\n\n");
        return;
    }
    struct Stack* Position = stack;
    struct Stack* newStack = createStack();
    printf("\nresult >\n");
    while(Position -> next != NULL){
        Position = Position -> next;
        push(newStack, Position -> line);
    }
    Position = newStack;
    while(Position -> next != NULL){
        Position = Position -> next;
        printf("%s\n", Position -> line);
    }
    printf("\n");
}

// This function uses the same functionality as print function but without printing, then prints the data
// from the new stack to the file.
void save(){
    FILE *outF = fopen("output.txt", "w");
    if(isEmpty(has)){
        fprintf(outF, "Empty stack\n");
        fclose(outF);
        return;
    }
    struct Stack* Position = has;
    struct Stack* newStack = createStack();
    while(Position -> next != NULL){
        Position = Position -> next;
        push(newStack, Position -> line);
    }
    Position = newStack;
    while(Position -> next != NULL){
        Position = Position -> next;
        fprintf(outF, "%s\n", Position -> line);
    }
    fprintf(outF, "\n");
    fclose(outF);
}

// This is the function which reads the (input or commands) from the user and responds depending on the data entered.
void cases(){
    printf("MyCommand > ");
    char operation[nax * nax];
    gets(operation);
    if(strlen(operation) > nax){
        printf("\nLine's length should be at most 100 characters\n\n");
        cases();
    }
    if(strcmp(operation, "information") == 0){
        infoMenu();
    }
    else if(strcmp(operation, "return") == 0){
        menu();
    }
    else if(strcmp(operation, "undo") == 0){
        pop(has, popped);
        print(has);
    }
    else if(strcmp(operation, "redo") == 0){
        pop(popped, has);
        print(has);
    }
    else if(strcmp(operation, "print") == 0){
        print(has);
    }
    else if(strcmp(operation, "save") == 0){
        save();
    }
    else if(strcmp(operation, "quit") == 0){
        printf("\nresult > Good Bye!\n");
        save();
        exit(0);
    }
    else{
        push(has, operation);
    }
    cases();
}

// This is a menu that explains for the user what the commands do.
void infoMenu(){
    clear_screan();
    printf("\n\t\t_____________________________ Welcome to project #2 _________________________________\n");
    printf("\n\t\t\tIn this project you can enter strings, print them, undo and redo operations\n");
    printf("\n\t\t_____________________________________________________________________________________\n");
    printf("\n\t\t1. undo: this will undo (i.e., remove) that last entered statement\n");
    printf("\n\t\t2. redo: this will redo (i.e., re-add) that last removed statement\n");
    printf("\n\t\t3. print: this command will print the entire stored input text\n");
    printf("\n\t\t4. save: will save the text to a file called (output.txt)\n");
    printf("\n\t\t5. quit: will exit the program and save all results to (output.txt)\n");
    printf("\n\t\t_____________________________________________________________________________________\n");
    printf("\n\t\tType OK when you're done: ");
    char operation[nax];
    gets(operation);
    while(strcmp(operation, "OK") != 0){
        printf("Type OK when you're done: ");
        infoMenu();
    }
    clear_screan();
    cases();
}

// This is the main menu of the program.
void menu(){
    clear_screan();
    printf("\n\t\t_____________________________ Welcome to project #2 _________________________________\n");
    printf("\n\t\t\tIn this project you can enter strings, print them, undo and redo operations\n");
    printf("\n\t\t_____________________________________________________________________________________\n");
    printf("\n\t\t1. add\n");
    printf("\n\t\t2. undo\n");
    printf("\n\t\t3. redo\n");
    printf("\n\t\t4. print\n");
    printf("\n\t\t5. save\n");
    printf("\n\t\t6. quit\n");
    printf("\n\t\t7. To read about the functionalities of the above functions type information in the command\n");
    printf("\n\t\t8. You can return to this menu by typing return in the command\n");
    printf("\n\t\t_____________________________________________________________________________________\n");
    printf("\n\t\tType OK when you're done: ");
    char done[nax];
    gets(done);
    while(strcmp(done, "OK") != 0){
        printf("Type OK when you're done: ");
        menu();
    }
    clear_screan();
    cases();
}

// In the main function, we created the stacks "has" and "popped" then proceeded with the program.
int main(){
    has = createStack();
    popped = createStack();
    menu();
    return 0;
}
