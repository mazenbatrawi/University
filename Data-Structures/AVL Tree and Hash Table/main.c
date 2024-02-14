/*
Mazen Batrawi - 1190102
Section 2
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>

#define nax 100 // Max size
#define bool int // For simplicity, I used bool as a boolean
#define N 1000000

void menu();
void cases(int);
void hashMenu();
void hashCases(int);
void addNewPatient(bool);

bool isPrime[N]; // Denotes whether the number is prime or not.
int numOfPatients;
int filled, tableSize; // filled denotes how many patients are in the hash table.
bool not; // This boolean is used to check if the tree was loaded to the hash table.
FILE *inF, *outF;

// This is the function that makes each number from 0 to 1 million a prime or a composite number.
// At first we assume that all numbers are primes, except 0 and 1.
// Then we iterate over the numbers, if the number is a prime then all of it's multiples are composite so we mark them as composite.

void sieve(){
    for(int i = 0; i < N; i++){
        isPrime[i] = 1;
    }
    isPrime[0] = 0;
    isPrime[1] = 0;
    for(int i = 2; i < N; i++){
        if(isPrime[i]){
            for(int j = i + i; j < N; j += i){
                isPrime[j] = 0;
            }
        }
    }
}

typedef struct Tree* Tree;

// This is the AVL Tree we're using to store the data from the file.
struct Tree{
    char name[nax];
    char gender[10];
    int dayOfBirth, dayOfAdmission;
    int monthOfBirth, monthOfAdmission;
    int yearOfBirth, yearOfAdmission;
    char infection[nax];
    char address[nax];
    char bloodType[4];
    Tree left;
    Tree right;
    int height;
};

typedef struct HashTable* Table;

// This is the hash table with the name as the key.
struct HashTable{
    int status; // 0 Empty, 1 Filled, 2 Deleted
    char key[nax];
    char gender[10];
    int dayOfBirth, dayOfAdmission;
    int monthOfBirth, monthOfAdmission;
    int yearOfBirth, yearOfAdmission;
    char infection[nax];
    char address[nax];
    char bloodType[4];
};
void fillTable(Table, char*, char*, char*, char*, char*, int, int, int, int, int, int, int);

Tree Hospital; // The AVL Tree
Table table; // The Hash Table
char bloodTypes[12][5] = {"A", "A+", "A-", "B", "B+", "B-", "O", "O+", "O-", "AB", "AB+", "AB-"};
bool doneReading; // To check if the file was read before.

// This function clears the contents of the screen.
void clear_screen(){
    system("cls");
}

// This function pauses the screen until the user presses Enter on the keyboard.
void end_screen(int x){
    char op[nax];
    printf("\n\nPress Enter to go back to the menu ... ");
    fflush(stdin);
    if(x)
        gets(op);
    gets(op);
    clear_screen();
}

// Returns the height of the node in the tree.
int height(Tree node){
    if(node == NULL){
        return -1;
    }
    return node -> height;
}

// Returns the balance factor based on the direction we're moving to.
int getBalanceFactor(Tree node, bool dir){
    if (node == NULL){
        return 0;
    }
    int x = height(node -> right) - height(node -> left);
    return dir ? x : -x;
}

// Returns the maximum between two integers.
int max(int a, int b){
    if(a > b){
        return a;
    }
    return b;
}

/* If the tree needs balancing by a single left rotate, we call this function.
 Imagine if we have this situation:
a
 \
  b
   \
    c
then the tree is unbalanced because height(right) - height(left) of the root is not between -1 and 1, so we re balance the tree by making b the root.
(Left rotation)
  b
 / \
a   c

The same works for the single right rotation, but the initial state is like this:
     c
    /
   b
  /
 a
 Then it becomes like this
   b
  / \
 a   c
*/

Tree singleRotateLeft(Tree node){
    Tree temp;
    temp  = node -> right;
    node -> right  = temp -> left;
    temp -> left = node;
    node -> height = 1 + max(height(node -> left) , height(node -> right));
    temp -> height = 1 + max(height(node) , height(temp -> right));
    return temp;
}

Tree singleRotateRight(Tree node){
    Tree temp;
    temp  = node -> left;
    node -> left  = temp -> right;
    temp -> right = node;
    node -> height = 1 + max(height(node -> right) , height(node -> left));
    temp -> height = 1 + max(height(node) , height(temp -> left));
    return temp;
}

/*
Imagine if we have a situation like this:
    a
     \
     c
    /
    b
If we perform a single left rotation we will stay stuck in the same situation.
So we put the middle value as a root (which is b) and make the others as children (left right rotation)

  b
 / \
a   c
The same goes for the double rotate right but in an opposite way (right left rotation)
  c
 /
a
 \
  b

  b
 / \
a   c

*/

Tree doubleRotateLeft(Tree node){
    node -> left = singleRotateLeft(node -> left);
    return singleRotateRight(node);
}

Tree doubleRotateRight(Tree node){
    node -> right  = singleRotateRight(node -> right);
    return singleRotateLeft(node);
}

// Returns the day from the given date.
int getDay(char* date){
    if(strlen(date) >= 0 && strlen(date) <= 8){
        for(int i = 0; i < 2; i++){
            if(!isdigit(date[i])){
                return -1;
            }
        }
        int day = (date[0] - '0') * 10 + date[1] - '0';
        return day;
    }
    return -1;
}

// Returns the month from the given date.
int getMonth(char date[nax / 10]){
    if(strlen(date) >= 2 && strlen(date) <= 8){
        for(int i = 2; i < 4; i++){
            if(!isdigit(date[i])){
                return -1;
            }
        }
        int month = (date[2] - '0') * 10 + date[3] - '0';
        return month;
    }
    return -1;
}

// Returns the year from the given date.
int getYear(char date[nax / 10]){
    if(strlen(date) >= 4 && strlen(date) <= 8){
        for(int i = 4; i < 8; i++){
            if(!isdigit(date[i])){
                return -1;
            }
        }
        int year = (date[4] - '0') * 1000 + (date[5] - '0') * 100  + (date[6] - '0') * 10 + date[7] - '0';
        return year;
    }
    return -1;
}

// Returns 1 if the day, month, year in the date and the format of the date are correct, 0 otherwise.
bool check(int x, int y, int z, char* date){
    return x != -1 && y != -1 && z != -1 && strlen(date) == 8 && 1 <= x && x <= 31 && 1 <= y && y <= 12;
}

// Prints the given patient from the tree.
void printPatient(Tree node){
    printf("Patient's name: %s\nPatient's gender: %s\nPatient's date of admission: %d / %d / %d\nPatient's date of birth: %d / %d / %d\nPatient's infection: %s\nPatient's address: %s\nPatient's blood type: %s\n",
            node -> name, node-> gender, node -> dayOfAdmission, node -> monthOfAdmission, node -> yearOfAdmission,
            node -> dayOfBirth, node -> monthOfBirth, node -> yearOfBirth, node -> infection, node -> address, node -> bloodType);
}

/* This function adds a patient to the AVL tree. */
Tree insert(Tree patient, Tree node){
    if(node == NULL){ // The node is empty.
        Hospital = (Tree) malloc(sizeof(struct Tree));
        if(Hospital == NULL){
            clear_screen();
            printf("Out of memory, couldn't create the tree!\n\n");
            menu();
        }
        return patient;
    }
    int compare = strcasecmp(patient -> name, node -> name);
    if(compare < 0){ // The value is less than where we're at the tree so we move left.
        node -> left = insert(patient, node -> left);
        if(getBalanceFactor(node, 0) >= 2){ // Balancing the tree if an imbalance happens.
            if(strcasecmp(patient -> name, node -> left -> name) < 0){
                node = singleRotateRight(node);
            }
            else{
                node = doubleRotateLeft(node);
            }
        }
    }
    else if(compare > 0){ // The value is greater than where we're at the tree so we move right
        node -> right = insert(patient, node -> right);
        if(getBalanceFactor(node, 1) >= 2){ // Balancing the tree if an imbalance happens.
            if(strcasecmp(patient -> name, node -> right -> name) > 0)
                node = singleRotateLeft(node);
            else
                node = doubleRotateRight(node);
        }
    }
    node -> height = 1 + max(height(node -> left), height(node -> right)); // Giving the patient his height in the tree.
    return node;
}

// Printing the tree  in lexicographic order (which is In-order method, left root right).
void printInOrder(Tree node, bool emptyTree, bool inFile){ // The boolean checks if the tree is empty.
    if(emptyTree && node == NULL){
        clear_screen();
        if(inFile){
            fprintf(outF, "You don't have any patients.\n");
            return;
        }
        else{
            printf("You don't have any patients.\n");
            menu();
        }
        return;
    }
    if(node != NULL){
        printInOrder(node -> left, 0, inFile);
        if(inFile){
            fprintf(outF, "Patient's name: %s\nPatient's gender: %s\nPatient's date of admission: %d / %d / %d\nPatient's date of birth: %d / %d / %d\nPatient's infection: %s\nPatient's address: %s\nPatient's blood type: %s\n",
                node -> name, node-> gender, node -> dayOfAdmission, node -> monthOfAdmission, node -> yearOfAdmission,
                node -> dayOfBirth, node -> monthOfBirth, node -> yearOfBirth, node -> infection, node -> address, node -> bloodType);
            fprintf(outF, "____________________________________________________________________________________________________\n\n");
        }
        else{
            printf("Patient's name: %s\nPatient's gender: %s\nPatient's date of admission: %d / %d / %d\nPatient's date of birth: %d / %d / %d\nPatient's infection: %s\nPatient's address: %s\nPatient's blood type: %s\n",
                node -> name, node-> gender, node -> dayOfAdmission, node -> monthOfAdmission, node -> yearOfAdmission,
                node -> dayOfBirth, node -> monthOfBirth, node -> yearOfBirth, node -> infection, node -> address, node -> bloodType);
            printf("____________________________________________________________________________________________________\n\n");
        }
        printInOrder(node -> right, 0, inFile);
    }
}

// This functions reads the file of patients and adds them to the tree.
void read(){
    if(doneReading){
        printf("File was read before.\n");
        menu();
        return;
    }
    doneReading = 1;
    inF = fopen("patients.txt", "r");
    if(inF == NULL){
        printf("Couldn't open the file!\n\n");
        exit(0);
    }
    char line[nax];
    while(fgets(line, nax, inF) != NULL){
        char *token = strtok(line, "#");
        int cnt = 0;
        Tree node = (Tree) malloc(sizeof(struct Tree));
        if(node == NULL){
            clear_screen();
            printf("Out of memory!\n\n");
            return;
        }
        while(token != NULL){
            if(cnt == 0){
                strcpy(node -> name, token);
            }
            else if(cnt == 1){
                strcpy(node -> gender, token);
            }
            else if(cnt == 2){
                node -> dayOfAdmission = getDay(token);
                node -> monthOfAdmission = getMonth(token);
                node -> yearOfAdmission = getYear(token);
                if(!check(node -> dayOfAdmission, node -> monthOfAdmission, node -> yearOfAdmission, token)){
                    printf("The date of admission for the patient %s is invalid.\n", node -> name);
                    exit(0);
                }
            }
            else if(cnt == 3){
                node -> dayOfBirth = getDay(token);
                node -> monthOfBirth = getMonth(token);
                node -> yearOfBirth = getYear(token);
                if(!check(node -> dayOfBirth, node -> monthOfBirth, node -> yearOfBirth, token)){
                    printf("The date of birth for the patient %s is invalid.\n", node -> name);
                    exit(0);
                }
            }
            else if(cnt == 4){
                strcpy(node -> infection, token);
            }
            else if(cnt == 5){
                strcpy(node -> address, token);
            }
            else if(cnt == 6){
                if(strlen(token) < 1 || strlen(token) > 3){
                    printf("The blood type of the patient %s is invalid.\n", node -> name);
                    exit(0);
                }
                strcpy(node -> bloodType, token);
            }
            token = strtok(NULL, "#");
            cnt += 1;
        }
        node -> left = NULL;
        node -> right = NULL;
        node -> height = 0;
        numOfPatients += 1;
        Hospital = insert(node, Hospital);
    }
    fclose(inF);
}

// Tree menu
void menu(){
    printf("\n\t   ____________________________________Welcome to project #3 __________________________________________\n");
    printf("\n\t   In this project, you'll be able to create a hospital and add / remove patients to it and search them\n");
    printf("\n\t   ____________________________________________________________________________________________________\n");
    printf("\n\t   1. Read the file (patients.txt) and load the data to the tree.\n");
    printf("\n\t   2. Insert a new patient with all its associated data.\n");
    printf("\n\t   3. Find a patient and update the information of the patient if found.\n");
    printf("\n\t   4. List all patients in lexicographic order with their associated information.\n");
    printf("\n\t   5. List all patients that have the same illness.\n");
    printf("\n\t   6. Delete a patient from the system.\n");
    printf("\n\t   7. Save all words in file (patients_hash.data).\n");
    printf("\n\t   ____________________________________________________________________________________________________\n");
    printf("\n\t   Enter your choice: ");
    int n;
    scanf("%d", &n);
    while(n < 1 || n > 7){
        clear_screen();
        printf("Invalid choice, choose a number between 1 and 7.\n");
        menu();
    }
    clear_screen();
    cases(n);
}

// When we use the hash table, sometimes we need to increase the size of the hash table, so we re hash the current table.
// The new size = a prime >= 2 * old size + 1.
void reHash(){
    int newSize = 2 * tableSize + 1;
    while(!isPrime[newSize]){
        newSize += 1;
    }
    Table newTable = (Table) malloc(newSize * sizeof(struct HashTable));
    if(newTable == NULL){
        clear_screen();
        printf("Out of memory!\n\n");
        return;
    }
    for(int i = 0; i < newSize; i++){
        newTable[i].status = 0;
        if(i < tableSize && table[i].status == 2){
            newTable[i].status = 2;
        }
    }
    char name[nax], address[nax], infection[nax], bloodType[4], gender[10];
    int dayOfBirth, monthOfBirth, yearOfBirth, dayOfAdmission, monthOfAdmission, yearOfAdmission;
    for(int i = 0; i < tableSize; i++){
        if(table[i].status == 1){
            strcpy(name, table[i].key);
            strcpy(address, table[i].address);
            strcpy(infection, table[i].infection);
            strcpy(bloodType, table[i].bloodType);
            strcpy(gender, table[i].gender);

            dayOfBirth = table[i].dayOfBirth; monthOfBirth = table[i].monthOfBirth; yearOfBirth = table[i].yearOfBirth;
            dayOfAdmission = table[i].dayOfAdmission; monthOfAdmission = table[i].monthOfAdmission;
            yearOfAdmission = table[i].yearOfAdmission;

            fillTable(newTable, name, address, infection, bloodType, gender, dayOfAdmission, monthOfAdmission, yearOfAdmission,
                      dayOfBirth, monthOfBirth, yearOfBirth, newSize);
        }
    }
    table = newTable;
    tableSize = newSize;
}

// Adds a patient to the hash table depending on the sum of the characters of the name % table size.
void fillTable(Table table, char name[nax], char address[nax], char infection[nax], char bloodType[4], char gender[10],
         int dayOfAdmission, int monthOfAdmission, int yearOfAdmission, int dayOfBirth, int monthOfBirth, int yearOfBirth,int newSize){
    int value = 0;
    for(int i = 0; i < strlen(name); i++){
        if(name[i] == ' ') continue;
        value += name[i];
    }
    value %= newSize;
    while(table[value].status == 1){
        value += 1;
        value %= newSize;
    }
    table[value].status = 1;
    strcpy(table[value].key, name);
    strcpy(table[value].address, address);
    strcpy(table[value].infection, infection);
    strcpy(table[value].bloodType, bloodType);
    strcpy(table[value].gender, gender);
    table[value].dayOfBirth = dayOfBirth; table[value].monthOfBirth = monthOfBirth; table[value].yearOfBirth = yearOfBirth;
    table[value].dayOfAdmission = dayOfAdmission; table[value].monthOfAdmission = monthOfAdmission; table[value].yearOfAdmission = yearOfAdmission;
    double loadFactor = (double) (filled + 1) / newSize; // + 1 because I increase the value of filled after returning from this function not before.
    if(loadFactor >= 0.75){
        reHash();
    }
}

// This function is a recursive function that loads the tree to the hash table (In - order).
void getNode(Tree tree){
    if(tree == NULL){
        return;
    }
    getNode(tree -> left);
    char name[nax], address[nax], infection[nax], bloodType[4], gender[10];
    int dayOfBirth, dayOfAdmission, monthOfBirth, monthOfAdmission, yearOfBirth, yearOfAdmission;

    strcpy(name, tree -> name);
    strcpy(address, tree -> address);
    strcpy(infection, tree -> infection);
    strcpy(bloodType, tree -> bloodType);
    strcpy(gender, tree -> gender);
    dayOfBirth = tree -> dayOfBirth; monthOfBirth = tree -> monthOfBirth; yearOfBirth = tree -> yearOfBirth;
    dayOfAdmission = tree -> dayOfAdmission; monthOfAdmission = tree -> monthOfAdmission; yearOfAdmission = tree -> yearOfAdmission;
    fillTable(table, name, address, infection, bloodType, gender, dayOfAdmission, monthOfAdmission, yearOfAdmission,
                      dayOfBirth, monthOfBirth, yearOfBirth, tableSize);

    getNode(tree -> right);
}

// This function prints the hash table.
void printTable(Table table){
    for(int i = 0; i < tableSize; i++){
        if(table[i].status == 1){
            printf("Patient's name: %s\nPatient's gender: %s\nPatient's date of admission: %d / %d / %d\nPatient's date of birth: %d / %d / %d\nPatient's infection: %s\nPatient's address: %s\nPatient's blood type: %s\n",
                table[i].key, table[i].gender, table[i].dayOfAdmission, table[i].monthOfAdmission, table[i].yearOfAdmission,
                table[i].dayOfBirth, table[i].monthOfBirth, table[i].yearOfBirth, table[i].infection, table[i].address, table[i].bloodType);
            printf("____________________________________________________________________________________________________\n\n");

        }
        else if(table[i].status == 2){
            printf("Deleted record\n\n");
            printf("____________________________________________________________________________________________________\n\n");
        }
        else if(table[i].status == 0){
            printf("Empty record\n\n");
            printf("____________________________________________________________________________________________________\n\n");
        }
    }
}

// This function searches for a patient by key (name) in the hash table.
void searchPatient(){
    fflush(stdin);
    clear_screen();
    printf("Enter the patient's name: ");
    char name[nax];
    gets(name);
    int value = 0;
    for(int i = 0; i < strlen(name); i++){
        if(name[i] == ' ') continue;
        value += name[i];
    }
    value %= tableSize;
    int ind = value;
    bool cycle = 0, found = 0; // Cycle boolean checks if we visited this position in the hash table before, found boolean checks if the patient was found.
    while((table[value].status == 1 || table[value].status == 2) && (cycle || ind == value)){
        cycle = 1;
        if(strcasecmp(table[value].key, name) == 0){ // Patient found.
            printf("Patient's name: %s\nPatient's gender: %s\nPatient's date of admission: %d / %d / %d\nPatient's date of birth: %d / %d / %d\nPatient's infection: %s\nPatient's address: %s\nPatient's blood type: %s\n",
                table[value].key, table[value].gender, table[value].dayOfAdmission, table[value].monthOfAdmission, table[value].yearOfAdmission,
                table[value].dayOfBirth, table[value].monthOfBirth, table[value].yearOfBirth, table[value].infection, table[value].address, table[value].bloodType);
            end_screen(0);
            found = 1;
            break;
        }
        value += 1;
        value %= tableSize;
    }
    if(!found){ // Patient not found.
        clear_screen();
        printf("The patient wasn't found\n");
    }
    hashMenu();
}

// This function saves the hash table to the file (patients_hash.data.txt).
void saveHash(){
    outF = fopen("patients_hash.data.txt", "w");
    if(outF == NULL){
        printf("Couldn't open the file!\n\n");
        exit(0);
    }
    for(int i = 0; i < tableSize; i++){
        if(table[i].status == 0){
            fprintf(outF, "Empty record\n\n");
            fprintf(outF, "____________________________________________________________________________________________________\n\n");
        }
        else if(table[i].status == 2){
            fprintf(outF, "Deleted record\n\n");
            fprintf(outF, "____________________________________________________________________________________________________\n\n");
        }
        else if(table[i].status == 1){
            fprintf(outF, "Patient's name: %s\nPatient's gender: %s\nPatient's date of admission: %d / %d / %d\nPatient's date of birth: %d / %d / %d\nPatient's infection: %s\nPatient's address: %s\nPatient's blood type: %s\n",
                table[i].key, table[i].gender, table[i].dayOfAdmission, table[i].monthOfAdmission, table[i].yearOfAdmission,
                table[i].dayOfBirth, table[i].monthOfBirth, table[i].yearOfBirth, table[i].infection, table[i].address, table[i].bloodType);
            fprintf(outF, "____________________________________________________________________________________________________\n\n");
        }

    }
    fclose(outF);
    printf("Saved successfully!\n\t____________________________________ Goodbye! _________________________________________________\n");
    exit(0);
}

// This function deletes a patient from the table by searching for it by key(name).
void deleteFromTable(Table table){
    fflush(stdin);
    clear_screen();
    char name[nax];
    printf("Enter the name of the patient: ");
    gets(name);
    clear_screen();
    int value = 0;
    for(int i = 0; i < strlen(name); i++){
        if(name[i] == ' ') continue;
        value += name[i];
    }
    value %= tableSize;
    bool cycle = 0, found = 0;
    int ind = value;
    while((table[value].status == 1 || table[value].status == 2) && (cycle || ind == value)){
        cycle = 1;
        if(strcasecmp(table[value].key, name) == 0){
            table[value].status = 2;
            memset(table[value].key, '\0', sizeof(table[value].key));
            found = 1;
            printf("Record deleted successfully\n");
            filled -= 1;
            break;
        }
        value += 1;
        value %= tableSize;
    }
    if(!found){
        printf("Patient not found\n\n");
    }
}

// The hash menu
void hashMenu(){
    if(!not){ // Loading the tree to the hash table for the first time.
        getNode(Hospital);
    }
    not = 1;
    printf("\n\t____________________________________ Welcome to project #3 _________________________________________________\n");
    printf("\n\tIn this project, you'll be able to add / remove patients to a hash table and do some operations on the table\n");
    printf("\n\t____________________________________________________________________________________________________________\n");
    printf("\n\t1. Print hashed table.\n");
    printf("\n\t2. Print out hash table size.\n");
    printf("\n\t3. Print out the used hash function.\n");
    printf("\n\t4. Insert a new record into the hash table.\n");
    printf("\n\t5. Search for a specific patient.\n");
    printf("\n\t6. Delete a specific record.\n");
    printf("\n\t7. Save hash table back to file.\n");
    printf("\n\t___________________________________________________________________________________________________________\n");
    printf("\n\tEnter your choice: ");
    int n;
    scanf("%d", &n);
    while(n < 1 || n > 7){
        clear_screen();
        printf("Invalid choice, choose a number between 1 and 7.\n");
        hashMenu();
    }
    hashCases(n);
}

// Cases from the user's selection for the hash menu.
void hashCases(int n){
    clear_screen();
    switch(n){
        case 1:
            printTable(table);
            end_screen(0);
            hashMenu();
            break;
        case 2:
            printf("The table size is: %d\n", tableSize);
            end_screen(0);
            hashMenu();
            break;
        case 3:
            printf("The hashing function is: h[i](key) = [h(key) + i] %% tableSize");
            end_screen(0);
            hashMenu();
            break;
        case 4:
            addNewPatient(1);
            filled += 1;
            clear_screen();
            printf("Patient added successfully!\n\n");
            hashMenu();
            break;
        case 5:
            searchPatient();
            end_screen(0);
            break;
        case 6:
            deleteFromTable(table);
            hashMenu();
            break;
        case 7:
            saveHash();
            break;
    }
}

// This function adds a new patient to the tree and the hash table depending on the boolean ask.
// If ask is 1, then we need to add this patient to the hash table, otherwise, to the tree.
void addNewPatient(bool ask){
    char name[nax], address[nax], infection[nax], bloodType[3], dateOfBirth[nax / 10], dateOfAdmission[nax / 10], gender[10];
    int dayOfBirth, dayOfAdmission, monthOfBirth, monthOfAdmission, yearOfBirth, yearOfAdmission;
    printf("Enter the patient's name: ");
    fflush(stdin);
    gets(name);
    printf("Enter the patient's gender: ");
    gets(gender);
    while(strcmp(gender, "M") && strcmp(gender, "F")){
        printf("Type M for male and F for female: ");
        gets(gender);
    }
    printf("Enter the patient's date of admission: ");
    gets(dateOfAdmission);
    dayOfAdmission = getDay(dateOfAdmission);
    monthOfAdmission = getMonth(dateOfAdmission);
    yearOfAdmission = getYear(dateOfAdmission);
    while(!check(dayOfAdmission, monthOfAdmission, yearOfAdmission, dateOfAdmission)){
        printf("The date format is incorrect. It should be DDMMYYYY.\nEnter the patients date of admission: ");
        gets(dateOfAdmission);
        dayOfAdmission = getDay(dateOfAdmission);
        monthOfAdmission = getMonth(dateOfAdmission);
        yearOfAdmission = getYear(dateOfAdmission);
    }
    printf("Enter the patient's date of birth: ");
    gets(dateOfBirth);
    dayOfBirth = getDay(dateOfBirth);
    monthOfBirth = getMonth(dateOfBirth);
    yearOfBirth = getYear(dateOfBirth);
    while(!check(dayOfBirth, monthOfBirth, yearOfBirth, dateOfBirth)){
        printf("The date format is incorrect. It should be DDMMYYYY.\nEnter the patients date of birth: ");
        gets(dateOfBirth);
        dayOfBirth = getDay(dateOfBirth);
        monthOfBirth = getMonth(dateOfBirth);
        yearOfBirth = getYear(dateOfBirth);
    }
    printf("Enter the patient's infection: ");
    gets(infection);
    printf("Enter the patient's address: ");
    gets(address);
    printf("Enter the patient's blood type: ");
    gets(bloodType);
    bool correctBloodType = 0;
    while(1){
        for(int i = 0; i < 12; i++){
            if(!strcmp(bloodTypes[i], bloodType)){
                correctBloodType = 1;
            }
        }
        if(correctBloodType) break;
        printf("The blood type should be one of these: ");
        for(int i = 0; i < 12; i++){
            printf("%s", bloodTypes[i]);
            if(i != 11){
                printf(", ");
            }
        }
        printf("\nEnter the blood type: ");
        gets(bloodType);
    }
    if(ask){
        fillTable(table, name, address, infection, bloodType, gender
                , dayOfAdmission, monthOfAdmission, yearOfAdmission, dayOfBirth, monthOfBirth, yearOfBirth, tableSize);
        return;
    }
    Tree patient = (Tree) malloc(sizeof(struct Tree));
    strcpy(patient -> name, name);
    strcpy(patient -> gender, gender);
    patient -> dayOfBirth = dayOfBirth; patient -> monthOfBirth = monthOfBirth; patient -> yearOfBirth = yearOfBirth;
    patient -> dayOfAdmission = dayOfAdmission; patient -> monthOfAdmission = monthOfAdmission; patient -> yearOfAdmission = yearOfAdmission;
    strcpy(patient -> infection, infection);
    strcpy(patient -> address, address);
    strcpy(patient -> bloodType, bloodType);
    patient -> left = NULL;
    patient -> right = NULL;
    Hospital = insert(patient, Hospital);
    numOfPatients += 1;
}

// Searches for a patient in the tree by checking if the value of the current node if it is greater or less or equal to the patient's node.
// And moves upon the cases.
Tree find(char* name, Tree tree){
    if (tree == NULL){ // Reached the end of the tree and the patient is not found.
        return NULL;
    }
    if (strcasecmp(name, tree -> name) > 0){ // The value of the patient is greater than the current node so we move right.
        return find(name, tree -> right);
    }
    else if (strcasecmp(name, tree -> name) < 0){ // The value of the patient is less than the current node so we move left.
        return find (name, tree -> left);
    }
    else{ // Found the patient.
        return tree;
    }
}

// We use this function when we delete a node from the tree, to get to the left most of the passed node.
Tree min(Tree node){
    Tree current = node;
    while (current -> left != NULL){
        current = current -> left;
    }
    return current;
}

// This function deletes a node(patient) from the tree.
Tree deleteNode(char* name, Tree root){
    // Patient not found.
    if (root == NULL){
        return NULL;
    }
    // Standard AVL delete.
    if (strcasecmp(name, root -> name) < 0){ // The value of the patient is less than the current node so we move left.
        root -> left = deleteNode(name, root -> left);
    }
    else if(strcasecmp(name, root -> name) > 0){ // The value of the patient is greater than the current node so we move right.
        root -> right = deleteNode(name, root -> right);
    }
    else{ // Found the patient.
        if(root -> left == NULL || root -> right == NULL){ // If the node has 0 children or 1 child.
            Tree temp = root -> left ? root -> left : root -> right;
            if (temp == NULL){ // Case of 0 children for the node.
                temp = root;
                root = NULL;
            }
            else{ // Case of one child for the node.
                *root = *temp;
            }
            free(temp);
        }
        else{ // The node has two children, so we get the smallest in the right subtree.
            Tree temp = min(root -> right);
            strcpy(root -> name, temp -> name);
            strcpy(root -> gender, temp -> gender);
            strcpy(root -> infection, temp -> infection);
            strcpy(root -> bloodType, temp -> bloodType);
            strcpy(root -> address, temp -> address);
            root -> dayOfBirth = temp -> dayOfBirth; root -> monthOfBirth = temp -> monthOfBirth; root -> yearOfBirth = temp -> yearOfBirth;
            root -> dayOfAdmission = temp -> dayOfAdmission; root -> monthOfAdmission = temp -> monthOfAdmission; root -> yearOfAdmission = temp -> yearOfAdmission;
            root -> right = deleteNode(temp -> name, root -> right);
        }
    }
    // If the tree had only one node
    if (root == NULL){
        return root;
    }
    // Update the height of the current node.
    root -> height = max(height(root -> left), height(root -> right));
    // Checking the balance factor after the deletion.
    int balance = getBalanceFactor(root, 0);
    // Left Left Case
    if (balance > 1 && getBalanceFactor(root -> left, 0) >= 0){
        return singleRotateRight(root);
    }
    // Left Right Case
    if (balance > 1 && getBalanceFactor(root -> left, 0) < 0){
        return doubleRotateLeft(root);
    }
    // Right Right Case
    if (balance < -1 && getBalanceFactor(root -> right, 0) <= 0){
        return singleRotateLeft(root);
    }
    // Right Left Case
    if (balance < -1 && getBalanceFactor(root -> right, 0) > 0){
        return doubleRotateRight(root);
    }
    return root;
}

// Calls the function deleteNode which deletes the patient from the tree.
// Boolean ask is to specify if we want to update the user's information instead of deleting it.
void deletePatient(char* name, bool ask){
    numOfPatients -= 1;
    if(ask){
        printf("\nDo you want to update the patient's information? ");
        char op[nax];
        fflush(stdin);
        gets(op);
        while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
            printf("\nEnter YES or NO: ");
            gets(op);
        }
        if(!strcmp(op, "YES")){
            Hospital = deleteNode(name, Hospital);
            addNewPatient(0);
        }
        return;
    }
    Hospital = deleteNode(name, Hospital);
    printf("Patient deleted successfully!\n\n");
}

// This function asks the user if they want to update the patients information one by one.
// The node is deleted and added only if the name was changed.
void askToupdate(Tree patient){
    Tree newPatient = (Tree) malloc(sizeof(struct Tree));
    printf("Do you want to change the name? ");
    fflush(stdin);
    bool changeName = 0;
    char op[nax];
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        gets(op);
    }
    char newName[nax];
    strcpy(newName, patient -> name);
    if(strcmp(op, "YES") == 0){
        changeName = 1;
        printf("Enter the new name: ");
        gets(newName);
    }
    strcpy(newPatient -> name, newName);

    printf("Do you want to change the gender? ");
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        gets(op);
    }
    char newGender[nax];
    strcpy(newGender, patient -> gender);
    if(strcmp(op, "YES") == 0){
        printf("Enter the new gender: ");
        gets(newGender);
        while(strcmp(newGender, "M") && strcmp(newGender, "F")){
            printf("Type M for male and F for female: ");
            gets(newGender);
        }
    }
    strcpy(patient -> gender, newGender);
    if(changeName) strcpy(newPatient -> gender, newGender);
    printf("Do you want to change the date of admission? ");
    fflush(stdin);
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        fflush(stdin);
        gets(op);
    }
    char newDateOfAdmission[nax];
    int dayA, monthA, yearA;
    dayA = patient -> dayOfAdmission;
    monthA = patient -> monthOfAdmission;
    yearA = patient -> yearOfAdmission;
    if(strcmp(op, "YES") == 0){
        printf("Enter the new date of admission: ");
        fflush(stdin);
        gets(newDateOfAdmission);
        dayA = getDay(newDateOfAdmission);
        monthA = getMonth(newDateOfAdmission);
        yearA = getYear(newDateOfAdmission);
        while(!check(dayA, monthA, yearA, newDateOfAdmission)){
            printf("The date format is incorrect. It should be DDMMYYYY.\nEnter the patients date of admission: ");
            fflush(stdin);
            gets(newDateOfAdmission);
            dayA = getDay(newDateOfAdmission);
            monthA = getMonth(newDateOfAdmission);
            yearA = getYear(newDateOfAdmission);
        }
    }
    patient -> dayOfAdmission = dayA;
    patient -> monthOfAdmission = monthA;
    patient -> yearOfAdmission = yearA;
    if(changeName){
        newPatient -> dayOfAdmission = dayA;
        newPatient -> monthOfAdmission = monthA;
        newPatient -> yearOfAdmission = yearA;
    }
    fflush(stdin);
    printf("Do you want to change the date of birth? ");
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        gets(op);
    }
    char newDateOfBirth[nax];
    int dayB, monthB, yearB;
    dayB = patient -> dayOfBirth;
    monthB = patient -> monthOfBirth;
    yearB = patient -> yearOfBirth;
    if(strcmp(op, "YES") == 0){
        printf("Enter the new date of birth: ");
        fflush(stdin);
        gets(newDateOfBirth);
        dayB = getDay(newDateOfBirth);
        monthB = getMonth(newDateOfBirth);
        yearB = getYear(newDateOfBirth);
        while(!check(dayB, monthB, yearB, newDateOfBirth)){
            printf("The date format is incorrect. It should be DDMMYYYY.\nEnter the patients date of birth: ");
            fflush(stdin);
            gets(newDateOfBirth);
            dayB = getDay(newDateOfBirth);
            monthB = getMonth(newDateOfBirth);
            yearB = getYear(newDateOfBirth);
        }
    }
    patient -> dayOfBirth = dayB;
    patient -> monthOfBirth = monthB;
    patient -> yearOfBirth = yearB;
    if(changeName){
        newPatient -> dayOfBirth = dayA;
        newPatient -> monthOfBirth = monthA;
        newPatient -> yearOfBirth = yearA;
    }
    printf("Do you want to update the infection? ");
    fflush(stdin);
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        fflush(stdin);
        gets(op);
    }
    char infection[nax];
    strcpy(infection, patient -> infection);
    if(strcmp("YES", op) == 0){
        printf("Enter the new infection: ");
        fflush(stdin);
        gets(infection);
    }
    strcpy(patient -> infection, infection);
    if(changeName) strcpy(newPatient -> infection, infection);
    printf("Do you want to update the address? ");
    fflush(stdin);
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        fflush(stdin);
        gets(op);
    }
    char address[nax];
    strcpy(address, patient -> address);
    if(strcmp("YES", op) == 0){
        printf("Enter the new address: ");
        fflush(stdin);
        gets(address);
    }
    strcpy(patient -> address, address);
    if(changeName) strcpy(newPatient -> address, address);
    printf("Do you want to update the blood type? ");
    fflush(stdin);
    gets(op);
    while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
        printf("\nEnter YES or NO: ");
        fflush(stdin);
        gets(op);
    }
    char bloodType[5];
    strcpy(bloodType, patient -> bloodType);
    if(strcmp("YES", op) == 0){
        bool correctBloodType = 0;
        fflush(stdin);
        printf("Enter the new blood type: ");
        gets(bloodType);
        while(1){
            for(int i = 0; i < 12; i++){
                if(!strcmp(bloodTypes[i], bloodType)){
                    correctBloodType = 1;
                }
            }
            if(correctBloodType) break;
            printf("The blood type should be one of these: ");
            for(int i = 0; i < 12; i++){
                printf("%s", bloodTypes[i]);
                if(i != 11){
                    printf(", ");
                }
            }
            printf("\nEnter the blood type: ");
            fflush(stdin);
            gets(bloodType);
        }
    }
    strcpy(patient -> bloodType, bloodType);
    if(changeName) strcpy(newPatient -> bloodType, bloodType);
    if(changeName){
        deletePatient(patient -> name, 0);
        Hospital = insert(newPatient, Hospital);
    }
}

// Gets the name of the patient to delete it or update it.
void getName(bool ask, bool update, bool deleteThem){
    printf("Enter the patient's name: ");
    char name[nax];
    fflush(stdin);
    gets(name);
    Tree node = find(name, Hospital);
    clear_screen();
    if(node == NULL){
        printf("Patient not found.\n");
        menu();
        return;
    }
    if(ask) printPatient(node);
    if(update){
        printf("Do you want to update the patient's information? ");
        char op[nax];
        gets(op);
        while(strcmp(op, "YES") != 0 && strcmp(op, "NO") != 0){
            printf("\nEnter YES or NO: ");
            gets(op);
        }
        if(strcmp("YES", op) == 0) askToupdate(node);
    }
    if(deleteThem){
        deletePatient(name, 0);
    }
}

// This function traverses the tree to search for patients with the given illness.
void traverseTree(Tree node, char* infection){
    if(node == NULL){
        return;
    }
    if(!strcasecmp(node -> infection, infection)){
        printPatient(node);
        printf("____________________________________________________________________________________________________\n\n");
    }
    traverseTree(node -> left, infection);
    traverseTree(node -> right, infection);
}

// This function gets the illness the user wants to search for.
void sameIllness(){
    fflush(stdin);
    clear_screen();
    printf("Enter the illness: ");
    char illness[nax];
    gets(illness);
    printf("\n");
    traverseTree(Hospital, illness);
}

// Cases for the tree menu.
void cases(int n){
    switch(n){
        case 1:
            read();
            printf("File read successfully.\n");
            menu();
            break;
        case 2:
            addNewPatient(0);
            clear_screen();
            printf("Patient added successfully.\n");
            menu();
            break;
        case 3:
            getName(1, 1, 0);
            clear_screen();
            printf("Success!\n\n");
            menu();
            break;
        case 4:
            printInOrder(Hospital, 1, 0);
            end_screen(0);
            menu();
            break;
        case 5:
            sameIllness();
            end_screen(0);
            menu();
            break;
        case 6:
            getName(0, 0, 1);
            menu();
            break;
        case 7:
            if(!doneReading){
                clear_screen();
                printf("You should read the file first!\n\n");
                menu();
            }
            outF = fopen("patients_hash.data.txt", "a");
            if(outF == NULL){
                printf("Couldn't open the file!\n\n");
                exit(0);
            }
            printInOrder(Hospital, 1, 1);
            fclose(outF);
            tableSize = (numOfPatients / 0.6);
            while(!isPrime[tableSize]) tableSize += 1;
            filled = numOfPatients;
            table = (Table) malloc(tableSize * sizeof(struct HashTable));
            if(table == NULL){
                clear_screen();
                printf("Out of memory, couldn't create the hash table!\n\n");
                menu();
                break;
            }
            hashMenu();
            break;
    }
}

int main(){
    sieve();
    menu();
    return 0;
}
