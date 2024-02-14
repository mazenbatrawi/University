#include <bits/stdc++.h>
#include "API.h"

using namespace std;

const int N = 16;
const int INF = 1e9 + 5;
int dx[4] = {1, -1, 0, 0};
int dy[4] = {0, 0, 1, -1};
vector<vector<int>> prevPath, currentPath, flood, targets = {{N / 2 - 1, N / 2 - 1}, {N / 2, N / 2 - 1}, {N / 2 - 1, N / 2}, {N / 2, N / 2}};
set<pair<int, int>> neighbours[N][N];
vector<vector<bool>> visited;
pair<int, int> previous[N][N];

int moveCells(int x, int y, int x0, int y0, int dir, pair<int, int> (*parent)[N], bool ok = 0){
    string path = "";
    int x00 = x0, y00 = y0;
    while(parent[x0][y0].first != -1){
        pair<int, int> p = parent[x0][y0];
        if(p.first == x0){
            if(y0 < p.second){
                path += 'R';
            }
            else{
                path += 'L';
            }
        }
        else{
            if(x0 < p.first){
                path += 'D';
            }
            else{
                path += 'U';
            }
        }
        x0 = p.first, y0 = p.second;
    }
    int must = 3 * (path[0] == 'L') + 2 * (path[0] == 'D') + 1 * (path[0] == 'R');
    while(dir != must){
        dir = (dir + 1) % 4;
        API::turnRight();
    }
    for(char c : path){
        if(ok){
            API::setColor(y00, N - x00 - 1, 'G');
        }
        pair<int, int> p = parent[x00][y00];
        x00 = p.first, y00 = p.second;
        if(c == 'L'){
            if(dir == 0){
                dir = 3; API::turnLeft();
            }
            else if(dir == 2){
                dir = 3; API::turnRight();
            }
            else if(dir == 3){

            }
            else{
                assert(false);
            }
        }
        else if(c == 'R'){
            if(dir == 0){
                dir = 1; API::turnRight();
            }
            else if(dir == 2){
                dir = 1; API::turnLeft();
            }
            else if(dir == 1){

            }
            else{
                assert(false);
            }
        }
        else if(c == 'U'){
            if(dir == 1){
                dir = 0; API::turnLeft();
            }
            else if(dir == 3){
                dir = 0; API::turnRight();
            }
            else if(dir == 0){

            }
            else{
                assert(false);
            }
        }
        else{
            if(dir == 1){
                dir = 2; API::turnRight();
            }
            else if(dir == 3){
                dir = 2; API::turnLeft();
            }
            else if(dir == 2){

            }
            else{
                assert(false);
            }
        }
        API::moveForward();
    }
    if(ok){
        API::setColor(y00, N - x00 - 1, 'G');
    }
    return dir;
}

int getPath(int x, int y, int x0, int y0, int dir, bool ok = 0){
    if(x == x0 && y == y0) return dir;
    vector<vector<int>> q;
    q.push_back({x, y});
    vector<vector<bool>> visited2(N, vector<bool> (N, 0));
    visited2[x][y] = 1;
    pair<int, int> parent[N][N];
    for(int i = 0; i < N;  i++) for(int j = 0; j < N; j++) parent[i][j] = {-1, -1};
    while(!q.empty()){
        vector<int> current = q[0];
        q.erase(q.begin());
        int xx = current[0], yy = current[1];
        for(pair<int, int> nei : neighbours[xx][yy]){
            int nx = nei.first, ny = nei.second;
            if(visited2[nx][ny]) continue;
            visited2[nx][ny] = 1;
            q.push_back({nx, ny});
            parent[nx][ny] = {xx, yy};
        }
    }
    assert(visited2[x0][y0]);
    return moveCells(x, y, x0, y0, dir, parent, ok);
}

bool check(int x, int y){
    return 0 <= x && x < N && 0 <= y && y < N;
}

bool checkLeft(int x, int y, int dir, pair<int, int> p){
    return !API::wallLeft() && check(x + p.first, y + p.second);
}

bool checkRight(int x, int y, int dir, pair<int, int> p){
    return !API::wallRight() && check(x + p.first, y + p.second);
}

bool checkFront(int x, int y, int dir, pair<int, int> p){
    return !API::wallFront() && check(x + p.first, y + p.second);
}

bool isGoal(int x, int y){
    return (x == N / 2 || x == N / 2 - 1) && (y == N / 2 || y == N / 2 - 1);
}

vector<vector<int>> getAround(int x, int y, int dir){
    vector<vector<int>> around;
    pair<int, int> p = API::getDir(dir, 0);
    if(checkFront(x, y, dir, p)){
        around.push_back({flood[N - (y + p.second) - 1][x + p.first], 0});
        neighbours[N - y - 1][x].insert({N - (y + p.second) - 1, x + p.first});
        neighbours[N - (y + p.second) - 1][x + p.first].insert({N - y - 1, x});
    }
    p = API::getDir(dir, 1);
    if(checkRight(x, y, dir, p)){
        around.push_back({flood[N - (y + p.second) - 1][x + p.first], 1});
        neighbours[N - y - 1][x].insert({N - (y + p.second) - 1, x + p.first});
        neighbours[N - (y + p.second) - 1][x + p.first].insert({N - y - 1, x});
    }
    p = API::getDir(dir, 2);
    if(checkLeft(x, y, dir, p)){
        around.push_back({flood[N - (y + p.second) - 1][x + p.first], 2});
        neighbours[N - y - 1][x].insert({N - (y + p.second) - 1, x + p.first});
        neighbours[N - (y + p.second) - 1][x + p.first].insert({N - y - 1, x});
    }
    if(previous[x][y].first != -1){
        int prevX = previous[x][y].first, prevY = previous[x][y].second;
        around.push_back({flood[N - prevY - 1][prevX], 3});
    }
    sort(around.begin(), around.end());
    return around;
}

void floodFill(int x, int y){
    vector<vector<int>> q;
    q.push_back({x, y});
    while(!q.empty()){
        vector<int> v = q[0];
        q.erase(q.begin());
        x = v[0], y = v[1];
        if(visited[x][y]){
            int mn = INF;
            for(pair<int, int> nei : neighbours[x][y]){
                int nx = nei.first, ny = nei.second;
                mn = min(mn, flood[nx][ny]);
            }
            if(flood[x][y] <= mn){
                flood[x][y] = mn + 1;
                API::setText(y, N - x - 1, to_string(flood[x][y]));
                for(pair<int, int> nei : neighbours[x][y]){
                    int nx = nei.first, ny = nei.second;
                    q.push_back({nx, ny});
                }
            }
        }
        else{
            int mn = INF;
            for(int a = 0; a < 4; a++){
                int nx = x + dx[a], ny = y + dy[a];
                if(!check(nx, ny)) continue;
                mn = min(mn, flood[nx][ny]);
            }
            if(flood[x][y] <= mn){
                flood[x][y] = mn + 1;
                API::setText(y, N - x - 1, to_string(flood[x][y]));
                for(int a = 0; a < 4; a++){
                    int nx = x + dx[a], ny = y + dy[a];
                    if(!check(nx, ny)) continue;
                    q.push_back({nx, ny});
                }
            }
        }
    }
}

void explore(){
    int x = 0, y = 0, dir = 0;
    for(auto &cell : targets){
        visited[cell[0]][cell[1]] = 0;
    }
    while(true){
        API::setColor(x, y, 'G');
        API::fixWalls(x, y, dir);
        visited[N - y - 1][x] = 1;
        currentPath.push_back({x, y});
        if(isGoal(x, y)) break;
        vector<vector<int>> around = getAround(x, y, dir);
        if(flood[N - y - 1][x] <= around[0][0]){
            floodFill(N - y - 1, x);
        }
        pair<int, int> p = API::getDir(dir, around[0][1]);
        if(around[0][1] == 1){
            dir = (dir + 1) % 4;
            API::turnRight();
        }
        else if(around[0][1] == 2){
            dir = ((dir - 1) % 4 + 4) % 4;
            API::turnLeft();
        }
        else if(around[0][1] == 3){
            dir = (dir + 2) % 4;
            API::turnRight();
            API::turnRight();
        }
        API::moveForward();
        previous[x + p.first][y + p.second] = {x, y};
        x += p.first, y += p.second;
    }
    if(currentPath == prevPath){
        dir = getPath(N - 1, 0, N - y - 1, x, dir);
        API::clearAllColor();
        while(dir != 0){
            dir = (dir + 1) % 4;
            API::turnRight();
        }
        vector<int> v;
        for(auto &c : targets){
            if(visited[c[0]][c[1]]){
                v = c;
                break;
            }
        }
        dir = getPath(v[0], v[1], N - 1, 0, dir, 1);
        return;
    }
    prevPath = currentPath;
    currentPath.clear();
    dir = getPath(N - 1, 0, N - y - 1, x, dir);
    while(dir != 0){
        dir = (dir + 1) % 4;
        API::turnRight();
    }
    explore();
}

void init(){
    visited = vector<vector<bool>> (N, vector<bool> (N, 0));
    flood = vector<vector<int>> (N, vector<int> (N, INF));
    for(int i = 0; i < N; i++) for(int j = 0; j < N; j++){
        previous[i][j] = {-1, -1};
    }
    vector<vector<int>> q;
    for(vector<int> &v : targets){
        q.push_back({0, v[0], v[1]});
        flood[v[0]][v[1]] = 0;
        API::setText(v[0], v[1], to_string(flood[v[0]][v[1]]));
    }
    while(!q.empty()){
        vector<int> v = q[0];
        q.erase(q.begin());
        int x = v[1], y = v[2], cost = v[0];
        for(int a = 0; a < 4; a++){
            int nx = x + dx[a], ny = y + dy[a];
            if(!check(nx, ny)) continue;
            if(cost + 1 >= flood[nx][ny]) continue;
            flood[nx][ny] = cost + 1;
            API::setText(nx, ny, to_string(flood[nx][ny]));
            q.push_back({cost + 1, nx, ny});
        }
        sort(q.begin(), q.end());
    }
}

int main(int argc, char* argv[]) {
    init();
    explore();
    return 0;
}
