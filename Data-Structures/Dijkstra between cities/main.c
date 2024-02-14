// O(E + V log E)

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>

#define nax 100
#define bool int

void menu();

FILE *inF, *outF;
int maxV = -1;
int *countChildren; // Array to count the number of children for each node.
bool doneReading; // To check if the file was read.

typedef struct route* route;

route **graph; // 2D array of struct route that stores the child and the cost and the city.

struct route{
    int cost, child;
    char city[nax];
};

typedef struct pair* pair;

// To connect two integers together.
struct pair{
    int first;
    int second;
};

struct Heap{
    pair *arr; // Array of pairs sorted depending on the first of the pair.
    int entriesCount; // Current number of elements in the heap.
    int size; // Size of the heap.
    int type; // 0 for min heap, 1 for max heap.
};
typedef struct Heap* Heap;

Heap createHeap(int, int);
void insert(Heap, pair);
void swapBottomTop(Heap, int);
void swapTopBottom(Heap, int);
pair pop(Heap);

// Clearing the screen or holding it.
void end_screen(int x){
    char op[nax];
    fflush(stdin);
    if(x){
        printf("\n\nPress Enter to go back to the menu ... ");
        gets(op);
    }
    system("cls");
}

// A function to create the heap.
Heap createHeap(int size, int type){
    Heap heap = (Heap) malloc(sizeof(struct Heap));
    if(heap == NULL){
        end_screen(0);
        printf("OUT OF MEMORY!\n");
        end_screen(1);
        return NULL;
    }
    heap -> type = type;
    heap -> entriesCount=0;
    heap -> size = size;
    heap -> arr = (pair *) malloc(size * sizeof(struct pair));
    if (heap -> arr == NULL){
        end_screen(0);
        printf("OUT OF MEMORY!\n");
        end_screen(1);
        return NULL;
    }
    return heap;
}

// A function to insert a new pair to the heap.
void insert(Heap heap, pair key){
    if(heap -> size > heap -> entriesCount){
        heap -> arr[heap -> entriesCount] = key;
        swapBottomTop(heap, heap -> entriesCount); // Swapping until the heap is sorted.
        heap -> entriesCount++;
    }
}

void swapBottomTop(Heap heap, int ind){
    int parent = (ind - 1) / 2; // The parent of an element in the heap is always at (myPosition - 1) / 2.
    if(heap -> arr[ind] < heap -> arr[parent]){
        pair temp = heap -> arr[parent];
        heap -> arr[parent] = heap -> arr[ind];
        heap -> arr[ind] = temp;
        swapBottomTop(heap, parent); // Swapping until the heap is sorted.
    }
}

void swapTopBottom(Heap heap, int parent){
    int leftChild = parent * 2 + 1; // The left child of the parent is always at (myPosition * 2 + 1).
    int rightChild = parent * 2 + 2; // The right child of the parent is always at (myPosition * 2 + 2).
    int min;
    if(leftChild >= heap -> entriesCount || leftChild < 0){ // Doesn't have a left child.
        leftChild = -1;
    }
    if(rightChild >= heap -> entriesCount || rightChild < 0){ // Doesn't have a right child.
        rightChild = -1;
    }
    if(leftChild != -1 && heap -> arr[leftChild] -> first < heap -> arr[parent] -> first){
        min = leftChild;
    }
    else{
        min = parent;
    }
    if(rightChild != -1 && heap -> arr[rightChild] -> first < heap -> arr[min] -> first){
        min = rightChild;
    }
    if(min != parent){ // Taking the minimum and making it the parent by swaps.
        pair temp = heap -> arr[min];
        heap -> arr[min] = heap -> arr[parent];
        heap -> arr[parent] = temp;
        swapTopBottom(heap, min); // Swapping until the heap is sorted.
    }
}

pair pop(Heap heap){
    if(!(heap -> entriesCount)){
        end_screen(0);
        printf("Heap is Empty\n");
        end_screen(1);
        pair p = (pair) malloc(sizeof(struct pair));
        if(p == NULL){
            end_screen(0);
            printf("OUT OF MEMORY!\n");
            end_screen(1);
            exit(-1);
        }
        p -> first = -1;
        p -> second = -1;
        return p;
    }
    // Making the last entry as the root and making swaps until the heap is sorted.
    pair toPop = heap -> arr[0];
    heap -> arr[0] = heap -> arr[heap -> entriesCount - 1];
    heap -> entriesCount--;
    swapTopBottom(heap, 0);
    return toPop;
}

// Returns the maximum between two numbers.
int max(int a, int b){
    return (a >= b ? a : b);
}

// To read the file and count the vertices and counting the child's of each node.
void read(){
    inF = fopen("segments.txt", "r");
    if(inF == NULL){
        printf("Couldn't open the file!\n\n");
        exit(0);
    }
    char line[nax];
    while(fgets(line, nax, inF) != NULL){
        char *token = strtok(line, " ");
        int x, y;
        if(!isdigit(token[0])){
            continue;
        }
        x = atoi(token);
        token = strtok(NULL, " ");
        if(!isdigit(token[0])){
            continue;
        }
        y = atoi(token);
        maxV = max(x, y);
    }
    fclose(inF);
    countChildren = malloc(sizeof *countChildren * maxV);
    if(countChildren == NULL){
        end_screen(0);
        printf("OUT OF MEMORY!\n");
        end_screen(1);
        exit(-1);
    }
    for(int i = 0; i < maxV; i++){
        countChildren[i] = 0;
    }
    inF = fopen("segments.txt", "r");
    if(inF == NULL){
        printf("Couldn't open the file!\n\n");
        exit(0);
    }
    while(fgets(line, nax, inF) != NULL){
        char *token = strtok(line, " ");
        int x, y;
        if(!isdigit(token[0])){
            continue;
        }
        x = atoi(token);
        token = strtok(NULL, " ");
        if(!isdigit(token[0])){
            continue;
        }
        y = atoi(token);
        x--;
        y--;
        countChildren[x] += 1;
        countChildren[y] += 1;
    }
    fclose(inF);
}

// Removes leading and trailing spaces from a string.
char *removeSpaces(char *str){
    char *end;
    while(isspace((unsigned char)*str)) str++;
    if(*str == 0){
        return str;
    }
    end = str + strlen(str) - 1;
    while(end > str && isspace((unsigned char)*end)) end--;
    end[1] = '\0';
    return str;
}

// Initializing the graph.
void init(){
    graph = malloc(sizeof *graph * maxV);
    if (graph){
        for(int i = 0; i < maxV; i++){
            graph[i] = malloc(sizeof *graph[i] * countChildren[i]);
            if(graph[i] == NULL){
                end_screen(0);
                printf("OUT OF MEMORY!\n");
                end_screen(1);
                exit(-1);
            }
            for(int j = 0; j < countChildren[i]; j++){
                route temp = (route) malloc(sizeof(struct route));
                if(temp == NULL){
                    end_screen(0);
                    printf("OUT OF MEMORY!\n");
                    end_screen(1);
                    exit(-1);
                }
                strcpy(temp -> city, "\0");
                temp -> cost = -1;
                graph[i][j] = temp;
            }
        }
    }
    else{
        end_screen(0);
        printf("OUT OF MEMORY!\n");
        end_screen(1);
        exit(-1);
    }
    inF = fopen("segments.txt", "r");
    if(inF == NULL){
        printf("Couldn't open the file!\n\n");
        exit(0);
    }
    char line[nax], city[nax];
    int pos[maxV];
    for(int i = 0; i < maxV; i++){
        pos[i] = 0;
    }
    while(fgets(line, nax, inF) != NULL){
        char *token = strtok(line, " ");
        int x, y, distance;
        memset(city, '\0', sizeof(city));
        if(!isdigit(token[0])){
            continue;
        }
        x = atoi(token);
        token = strtok(NULL, " ");
        if(!isdigit(token[0])){
            continue;
        }
        y = atoi(token);
        token = strtok(NULL, " ");
        while(!isdigit(token[0])){
            strcat(city, " ");
            strcat(city, token);
            token = strtok(NULL, " ");
        }
        strcpy(city ,removeSpaces(city));
        if(!isdigit(token[0])){
            continue;
        }
        distance = atoi(token);
        x--;
        y--;
        if(x == y || distance < 0) continue;
        strcpy(graph[x][pos[x]] -> city, city);
        graph[x][pos[x]] -> cost = distance;
        graph[x][pos[x]] -> child = y;

        strcpy(graph[y][pos[y]] -> city, city);
        graph[y][pos[y]] -> cost = distance;
        graph[y][pos[y]] -> child = x;
        pos[x] += 1;
        pos[y] += 1;
    }
    fclose(inF);
}

// To print the path of nodes of the minimum cost calculated by the Dijkstra's algorithm.
void print(int parent[], int x, int y, bool ok, bool file){
    if(x == y){
        if(!file) printf("%d ", x + 1);
        else fprintf(outF, "%d ", x + 1);
        if(!ok){
            if(!file) printf("-> ");
            else fprintf(outF, "-> ");
        }
        return;
    }
    print(parent, x, parent[y], 0, file);
    if(!file) printf("%d ", y + 1);
    else fprintf(outF, "%d ", y + 1);
    if(!ok){
        if(!file) printf("-> ");
        else fprintf(outF, "-> ");
    }
}

// To print the path of cities of the minimum cost calculated by the Dijkstra's algorithm.
void printc(int parent[], char city[][nax], int x, int y, bool ok, bool file){
    if(x == y){
        return;
    }
    printc(parent, city, x, parent[y], 0, file);
    if(!file) printf("%s ", city[y]);
    else fprintf(outF, "%s ", city[y]);
    if(!ok){
        if(!file) printf("-> ");
        else fprintf(outF, "-> ");
    }
}

// Dijkstra.
void dij(){
    end_screen(0);
    printf("Enter the id of the first city: ");
    int x;
    scanf("%d", &x);
    printf("Enter the id of the second city: ");
    int y;
    scanf("%d", &y);
    if(x > maxV || y > maxV || x < 1 || y < 1){
        end_screen(0);
        printf("The id's entered may not be in the file\n\n");
        menu();
        return;
    }
    x--;
    y--;
    int total = 0;
    while(1){
        int parent[maxV]; // To get the parent of the node.
        char street[maxV][nax]; // To get the city.
        Heap heap = createHeap(maxV * 3, 0); // Creating the heap, multiplying by 3 to avoid memory failure.
        pair temp = (pair) malloc(sizeof(struct pair));
        if(temp == NULL){
            end_screen(0);
            printf("OUT OF MEMORY!\n");
            end_screen(1);
            return;
        }
        temp -> first = 0;
        temp -> second = x;
        int dist[maxV]; // Saves the minimum cost.
        for(int i = 0; i < maxV; i++){
            dist[i] = -1;
        }
        insert(heap, temp);
        dist[x] = 0;
        while(heap -> entriesCount != 0){ // While the heap is not empty.
            pair p = pop(heap);
            // Traversing through the children of the node and adding them to the heap if the distance to one of them is less than the
            // current one.
            for(int i = 0; i < countChildren[p -> second]; i++){
                int newDistance = p -> first + graph[p -> second][i] -> cost;
                if(newDistance < dist[graph[p -> second][i] -> child] || dist[graph[p -> second][i] -> child] == -1){
                    dist[graph[p -> second][i] -> child] = p -> first + graph[p -> second][i] -> cost;
                    pair temp1 = (pair) malloc(sizeof(struct pair));
                    if(temp1 == NULL){
                        end_screen(0);
                        printf("OUT OF MEMORY!\n");
                        end_screen(1);
                        return;
                    }
                    temp1 -> first = p -> first + graph[p -> second][i] -> cost;
                    temp1-> second = graph[p -> second][i] -> child;
                    parent[graph[p -> second][i] -> child] = p -> second;
                    strcpy(street[graph[p -> second][i] -> child], graph[p -> second][i] -> city);
                    insert(heap, temp1);
                }
            }
        }
        if(dist[y] != -1){ // If we reached the target from the source.
            total = dist[y];
            end_screen(0);
            printf("To go from %d to %d, traversing through nodes will be as the following: ", x + 1, y + 1);
            print(parent, x, y, 1, 0);
            printf("\n\nTo go from %d to %d, traversing through cities will be as the following: ", x + 1, y + 1);
            printc(parent, street, x, y, 1, 0);
            printf("\n\nTotal cost = %d\n", total);
            printf("\n\nDo you want to save the path to the file (route.txt)? (YES / NO): ");
            char op[10];
            fflush(stdin);
            gets(op);
            while(strcasecmp(op, "YES") && strcasecmp(op, "NO")){
                printf("\nEnter YES or NO: ");
                fflush(stdin);
                gets(op);
            }
            if(!strcasecmp(op, "YES")){
                outF = fopen("route.txt", "a");
                if(outF == NULL){
                    end_screen(0);
                    printf("\nCouldn't open file route.txt\n");
                }
                else{
                    fprintf(outF, "To go from %d to %d, traversing through nodes will be as the following: ", x + 1, y + 1);
                    print(parent, x, y, 1, 1);
                    fprintf(outF, "\n\nTo go from %d to %d, traversing through cities will be as the following: ", x + 1, y + 1);
                    printc(parent, street, x, y, 1, 1);
                    fprintf(outF, "\n\nTotal cost = %d\n", total);
                    fprintf(outF, "________________________________________________________________________________________________ \n\n");
                    fclose(outF);
                }
            }
            total = 0;
        }
        else{
            end_screen(0);
            printf("There is no route between %d and %d.\n", x + 1, y + 1);
            end_screen(1);
            menu();
            return;
        }
        printf("\n\nDo you want to go to another city? (YES, NO): ");
        fflush(stdin);
        char op[10];
        gets(op);
        while(strcasecmp(op, "YES") && strcasecmp(op, "NO")){
            printf("Enter YES or NO: ");
            fflush(stdin);
            gets(op);
        }
        if(!strcasecmp(op, "YES")){
            printf("Enter the id of the new city: ");
            x = y;
            scanf("%d", &y);
            if(y > maxV || y < 1){
                end_screen(0);
                printf("The id's entered may not be in the file!\n\n");
                menu();
                return;
            }
            y--;
        }
        else{
            end_screen(0);
            menu();
            return;
        }
    }
}

void cases(int choice){
    switch(choice){
        case 1:
            if(doneReading){
                end_screen(0);
                printf("The file was read before!\n");
                menu();
                return;
            }
            read();
            init();
            end_screen(0);
            printf("File read Successfully!\n\n");
            doneReading = 1;
            menu();
            break;
        case 2:
            dij();
            menu();
            break;
        case 3:
            end_screen(0);
            printf("\n\t\t\t  __________________________ Goodbye _____________________________\n");
            exit(0);
    }
}

void menu(){
    printf("\n\t\t  __________________________ Welcome to project #5 _____________________________\n");
    printf("\n\t\t  In this project, you'll be able to compute the minimum cost between two cities\n");
    printf("\n\t\t  ______________________________________________________________________________\n");
    printf("\n\t\t  1. Read the file (segments.txt) and load the data.\n");
    printf("\n\t\t  2. Enter 2 points to compute the shortest path between them.\n");
    printf("\n\t\t  3. Exit.\n");
    printf("\n\t\t  ______________________________________________________________________________\n");
    printf("\n\t\t  Enter your choice: ");
    int choice;
    scanf("%d", &choice);
    while(choice < 1 || choice > 3){
        end_screen(0);
        printf("Your choice should be between 1 and 3!\n\n");
        menu();
    }
    cases(choice);
}

int main(){
    menu();
    return 0;
}
