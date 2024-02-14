#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>

#define nax 100

void cases(int);
void menu();

// The struct we're using to store the equations, it is displayed as a doubly linked list.
struct Node{
    double coefficient;
    int exponent;
    char operation;
    struct Node *next;
    struct Node *prev;
};

struct Node* arr[nax]; // Array of Nodes to store each equation.
int pos; // The starting position in the Array of nodes, it is incremented when a new equation is added.
int doneReading; // This is like a boolean to make sure that the reading operation does not happen more than once and messes up the data.

// This functions clears the content of the current screen.
void clear_screan(){
    system("cls");
}

// A function returns if the List is empty.
int isEmpty(struct Node* L){
    return L -> next == NULL;
}

// This function adds a new node to the list, it adds the coefficient and the power to the specified position in the list. 
void insert(struct Node* L, struct Node* Position, int power, double num){
    struct Node* node = (struct Node*) malloc(sizeof(struct Node));
    if(node == NULL) return;
    node -> coefficient = num;
    node -> exponent = power;
    node -> next = Position -> next;
    node -> prev = Position -> prev;
    Position -> next = node;
    Position -> prev = node -> prev;
}

// This function removes the white spaces in the entered equations to process with reading them easily.
void removeSpaces(char *str){
    int poss = 0;
    for (int i = 0; str[i]; i++){
        if (str[i] != ' '){
            str[poss++] = str[i];
        }
    }
    str[poss] = '\0';
}

// This is the function that reads the equations from the input file and stores them to the list.
// Some numbers were converted from string to double using built in functions.
// Others were converted to numbers by traversing through the number and getting its value.
void read(){
    doneReading = 1;
    FILE *inF = fopen("equations.txt", "r");
    char line[nax];
    while(fgets(line, nax, inF) != NULL){
        struct Node* L;
        L = (struct Node*) malloc(sizeof(struct Node)); // Allocating space for the list in the memory.
        L -> next = NULL;
        struct Node* position = L; // The position we're adding at in the list.
        removeSpaces(line);
        int n = strlen(line);
        n--;
        for(int i = 0; i < n; i++){
            int negative = (i - 1 >= 0 && line[i - 1] == '-'); // As a boolean, to specify if the number is negative.
            double num = 1;
            int power = 0;
            // A special case if the coefficient is (+-)1, we check if there is a power and add this part to the list.
            if(line[i] == 'x'){
                if(i + 1 < n && line[i + 1] == '^'){
                    i += 1;
                    int now = 1;
                    if(i + 1 < n && line[i] == '^'){
                        i++;
                        while(i < n && isdigit(line[i]) == 1){
                            power += now * (line[i] - '0');
                            now *= 10;
                            i++;
                        }
                        int newpower = 0;
                        while (power != 0) {
                            int remainder = power % 10;
                            newpower = newpower * 10 + remainder;
                            power /= 10;
                        }
                        i--;
                        power = newpower;
                    }
                }
                else{
                    power = 1;
                }
                insert(L, position, power, num * (negative == 1 ? -1 : 1));
                position = position -> next;
                continue;
            }
            // If the coefficient is not 1, we need to get the coefficient and the power and add them to the list.
            char strNum[nax];
            if(isdigit(line[i])){
                int j = 0;
                while(i < n && (isdigit(line[i]) || line[i]== '.')){
                    strNum[j] = line[i];
                    i++; j++;
                }
                i--;
                num = strtod(strNum, &(strNum));
                if(i + 1 < n && line[i + 1] == 'x'){
                    i += 2;
                    int now = 1;
                    if(i + 1 < n && line[i] == '^'){
                        i++;
                        while(i < n && isdigit(line[i]) == 1){
                            power += now * (line[i] - '0');
                            now *= 10;
                            i++;
                        }
                        int newpower = 0;
                        while (power != 0) {
                            int remainder = power % 10;
                            newpower = newpower * 10 + remainder;
                            power /= 10;
                        }
                        i--;
                        power = newpower;
                    }
                    else{
                        power = 1;
                    }
                }
                else{
                    power = 0;
                }
                insert(L, position, power, num * (negative == 1 ? -1 : 1));
                position = position -> next;
            }
        }
        arr[pos++] = L;
    }
    fclose(inF);
    clear_screan();
    printf("Success !\n");
}

// This function deletes the list and its existence.
void deleteList(struct Node* L){
    struct Node* P;
    struct Node* temp;
    P = L->next;
    L->next = NULL;
    while(P != NULL){
        temp = P -> next;
        free(P);
        P = temp;
    }
}

// This function makes a copy of a list.
void getCopy(struct Node* second, struct Node* Pos, struct Node* first){
    struct Node* Position = first;
    while(Position -> next != NULL){
        Position = Position -> next;
        insert(second, Pos, Position -> exponent, Position -> coefficient);
    }
}

// A function that prints the equation from the list.
// Some stuff was handled like if the number is a double and its value is equal
// to it as an integer, we print it as an integer for simplicity.
void printEquation(struct Node* equation){
    while(equation -> next != NULL){
        equation = equation -> next;
        int ok = (equation -> coefficient - floor(equation -> coefficient) == 0);
        int zero = equation -> exponent == 0;
        int one = equation -> exponent == 1;
        if(ok == 1){
            if(one == 1){
                if(equation -> coefficient == 1){
                    printf(" x");
                }
                else{
                    printf(" %.1lfx", equation -> coefficient);
                }
            }
            else if(zero == 1){
                printf(" %.1lf", equation -> coefficient);
            }
            else{
                printf(" %.1lfx^%d", equation -> coefficient, equation -> exponent);
            }
        }
        else{
            if(one == 1){
                if(equation -> coefficient == 1){
                    printf(" x");
                }
                else{
                    printf(" %.1lfx", equation -> coefficient);
                }
            }
            else if(zero == 1){
                printf(" %lf", equation -> coefficient);
            }
            else{
                printf(" %lfx^%d", equation -> coefficient, equation -> exponent);
            }
        }
        if(equation -> next != NULL && equation -> next -> coefficient > 0) printf(" +");
    }
}

// This function prints the equation to the file with the substituted value for the variable from the user.
// Some stuff was handled like if the number is a double and its value is equal
// to it as an integer, we print it as an integer for simplicity.
void printEquationFile(struct Node* equation, char op[], double n, double ans){
    FILE *outF = fopen("results.txt", "a");
    fprintf(outF, "The %s on all equations is: ", op);
    while(equation -> next != NULL){
        equation = equation -> next;
        int ok = (equation -> coefficient - floor(equation -> coefficient) == 0);
        int zero = equation -> exponent == 0;
        int one = equation -> exponent == 1;
        if(ok == 1){
            if(one == 1){
                if(equation -> coefficient == 1){
                    fprintf(outF, " x");
                }
                else{
                    fprintf(outF, " %.1lfx", equation -> coefficient);
                }
            }
            else if(zero == 1){
                fprintf(outF, " %.1lf", equation -> coefficient);
            }
            else{
                fprintf(outF, " %.1lfx^%d", equation -> coefficient, equation -> exponent);
            }
        }
        else{
            if(one == 1){
                if(equation -> coefficient == 1){
                    fprintf(outF, " x");
                }
                else{
                    fprintf(outF, " %.1lfx", equation -> coefficient);
                }
            }
            else if(zero == 1){
                fprintf(outF, " %lf", equation -> coefficient);
            }
            else{
                fprintf(outF, " %lfx^%d", equation -> coefficient, equation -> exponent);
            }
        }
        if(equation -> next != NULL && equation -> next -> coefficient > 0) fprintf(outF, " +");
    }
    fprintf(outF, "\nWhen substituting %.1lf, the answer is: %.1lf\n\n", n, ans);
    fclose(outF);
    clear_screan();
    printf("Success !\n");
}

// This function multiplies the equations with each other, at the beginning, a list with the value 1 is created.
// Then we multiply all equations with this list and save the new equation.
void multiply(){
    if(pos == 0){
        printf("No equations have been added yet!\n");
        menu();
        return;
    }
    struct Node* crnt = (struct Node*) malloc(sizeof(struct Node));
    crnt -> next = NULL;
    struct Node* tail = crnt;
    insert(crnt, tail, 0, 1);
    struct Node* ansList = (struct Node*) malloc(sizeof(struct Node));
    ansList -> next = NULL;
    struct Node* newTail = ansList;
    for (int i = 0; i < pos; i++){
        // The current equation as if it is the answer equation is stored in H.
        struct Node* H = crnt;
        struct Node* P;
        while (H -> next != NULL){
            // The current equation is stored in P.
            P = arr[i];
            double coeff = H -> next -> coefficient;
            int exp = H -> next -> exponent;
            // Multiplying all the equation in by the current part in H.
            while(P -> next != NULL){
                P = P -> next;
                insert(ansList, newTail, exp + P -> exponent, coeff * P -> coefficient);
            }
            H = H -> next;
        }
        // We delete the crnt list and move the ansList to crnt to prevent calculating extra terms, then we clear the ansList.
        deleteList(crnt);
        getCopy(crnt, tail, ansList);
        deleteList(ansList);
    }
    // This while loop removes the duplicates and adds all the coefficients of the same power together.
    struct Node* H = crnt;
    while(H -> next != NULL){
        struct Node* P = H;
        double coeff = P -> next -> coefficient;
        int exp = P -> next -> exponent;
        P = P -> next;
        while(P -> next != NULL){
            if (P -> next -> exponent == exp){
                coeff += P -> next -> coefficient;
                struct Node* temp = P -> next;
                P -> next = temp -> next;
                free(temp);
            }
            else{
                P = P -> next;
            }
        }
        insert(ansList, newTail, exp, coeff);
        H = H -> next;
    }
    printf("The multiplication on all equations is:");
    printEquation(ansList);
    // Calculating the answer when substituting the value of the variable.
    printf("\nPlease enter a value to substitute it in the equation: ");
    double n;
    scanf("%lf", &n);
    struct Node* Position = ansList;
    double ans = 0;
    while(Position -> next != NULL){
        Position = Position -> next;
        ans += Position -> coefficient * pow(n, Position -> exponent);
    }
    printf("The answer is: %lf\n", ans);
    // Finally, asking the user if he wants to save the answer and the equation to the file.
    printf("Do you want to save the equation and the answer to the file (results.txt)? (Yes/No): ");
    char c[nax];
    scanf("%s", c);
    while(strcmp(c, "Yes") != 0 && strcmp(c, "No") != 0){
        printf("Invalid input!\nEnter Yes or No: ");
        scanf("%s", c);
    }
    if(strcmp(c, "Yes") == 0){
        printEquationFile(ansList, "multiplication", n, ans);
    }
    clear_screan();
}

// This function stores the first equation as an answer and subtracts the other equatuions from it.
void subtract(){
    if(pos == 0){
        printf("No equations have been added yet!\n");
        menu();
        return;
    }
    // This array represents the subtraction of all coefficients of the current index (i).
    double coefficients[nax];
    for(int i = 0; i < nax; i++){
        coefficients[i] = 0;
    }
    for(int i = 0; i < pos; i++){
        struct Node* Position = arr[i];
        while(Position -> next != NULL){
            Position = Position -> next;
            int x = Position -> exponent;
            if(i == 0) coefficients[x] += Position -> coefficient * (Position -> operation == '-' ? -1 : 1);
            else coefficients[x] -= Position -> coefficient * (Position -> operation == '-' ? -1 : 1);
        }
    }
    // This is the final equation, we store the values in it from the array "coefficients".
    struct Node* eq = (struct Node*) malloc(sizeof(struct Node));
    eq -> next = NULL;
    struct Node* Position = eq;
    for(int i = nax - 1; i >= 0; i--){
        if(coefficients[i] != 0){
            insert(eq, Position, i, coefficients[i]);
        }
    }
    printf("The subtraction on all equations is:");
    printEquation(eq);
    printf("\nPlease enter a value to substitute it in the equation: ");
    double n;
    scanf("%lf", &n);
    Position = eq;
    double ans = 0;
    // Calculating the answer when substituting the value of the variable.
    while(Position -> next != NULL){
        Position = Position -> next;
        ans += Position -> coefficient * pow(n, Position -> exponent);
    }
    printf("The answer is: %lf\n", ans);
    // Finally, asking the user if he wants to save the answer and the equation to the file.
    printf("Do you want to save the equation and the answer to the file (results.txt)? (Yes/No): ");
    char c[nax];
    scanf("%s", c);
    while(strcmp(c, "Yes") != 0 && strcmp(c, "No") != 0){
        printf("Invalid input!\nEnter Yes or No: ");
        scanf("%s", c);
    }
    if(strcmp(c, "Yes") == 0){
        printEquationFile(eq, "subtraction", n, ans);
    }
    clear_screan();
}

// This function works exactly as the subtraction function, but the difference
// is the addition instead of subtraction.
void add(){
    if(pos == 0){
        printf("No equations have been added yet!\n");
        menu();
        return;
    }
    double coefficients[nax];
    for(int i = 0; i < nax; i++){
        coefficients[i] = 0;
    }
    for(int i = 0; i < pos; i++){
        struct Node* Position = arr[i];
        while(Position -> next != NULL){
            Position = Position -> next;
            int x = Position -> exponent;
            coefficients[x] += Position -> coefficient * (Position -> operation == '-' ? -1 : 1);
        }
    }
    struct Node* eq = (struct Node*) malloc(sizeof(struct Node));
    eq -> next = NULL;
    struct Node* Position = eq;
    for(int i = nax - 1; i >= 0; i--){
        if(coefficients[i] != 0){
            insert(eq, Position, i, coefficients[i]);
        }
    }
    printf("The addition on all equations is:");
    printEquation(eq);
    printf("\nPlease enter a value to substitute it in the equation: ");
    double n;
    scanf("%lf", &n);
    Position = eq;
    double ans = 0;
    while(Position -> next != NULL){
        Position = Position -> next;
        ans += Position -> coefficient * pow(n, Position -> exponent);
    }
    printf("The answer is: %lf\n", ans);
    printf("Do you want to save the equation and the answer to the file (results.txt)? (Yes/No): ");
    char c[nax];
    scanf("%s", c);
    while(strcmp(c, "Yes") != 0 && strcmp(c, "No") != 0){
        printf("Invalid input!\nEnter Yes or No: ");
        scanf("%s", c);
    }
    if(strcmp(c, "Yes") == 0){
        printEquationFile(eq, "addition", n, ans);
    }
    clear_screan();
}

// A menu for the user.
void menu(){
    int n;
    printf("\n\t\t_____________________________ Welcome to project #1 _________________________________\n");
    printf("\n\t\t\tIn this project you can enter equations and perform operations on them\n");
    printf("\n\t\t_____________________________________________________________________________________\n");
    printf("\n\t\t1. Read the file and store to the linked list.\n");
    printf("\n\t\t2. Perform addition on all equations.\n");
    printf("\n\t\t3. Perform subtraction on all equations.\n");
    printf("\n\t\t4. Perform multiplication on all equations.\n");
    printf("\n\t\t5. Perform addition, subtraction, and multiplication on all equations.\n");
    printf("\n\t\t6. Exit.\n");
    printf("\n\t\t_____________________________________________________________________________________\n");
    printf("\n\t\tEnter your selection: ");
    scanf("%d", &n);
    if(n < 1 || n > 6){
        clear_screan();
        printf("Your selection should be between 1 and 6 !\n");
        menu();
    }
    cases(n);
}

// This function acts depending on the input from the user.
void cases(int n){
    clear_screan();
    switch(n){
        case 1:
            if(doneReading == 1){
                clear_screan();
                printf("Equations were already stored in the lists\n");
                menu();
                break;
            }
            read();
            menu();
            break;
        case 2:
            add();
            menu();
            break;
        case 3:
            subtract();
            menu();
            break;
        case 4:
            multiply();
            menu();
            break;
        case 5:
            add();
            subtract();
            multiply();
            menu();
            break;
        case 6:
            printf("\n\t\t____________________________________ Goodbye ________________________________________\n");
            exit(0);
            break;
    }
}

int main(){
    menu();
    return 0;
}
