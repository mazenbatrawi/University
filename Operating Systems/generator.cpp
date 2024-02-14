#include<bits/stdc++.h>

using namespace std;

#define uid(a, b) uniform_int_distribution<int>(a, b)(rng)
mt19937 rng(chrono::steady_clock::now().time_since_epoch().count());
#define inF freopen("input.txt", "r", stdin);
#define outF freopen("output.txt", "w", stdout);

vector<int> v = {40, 200, 500};

char reVal(int num)
{
    if (num >= 0 && num <= 9)
        return (char)(num + '0');
    else
        return (char)(num - 10 + 'A');
}

void strev(char *str)
{
    int len = strlen(str);
    int i;
    for (i = 0; i < len/2; i++)
    {
        char temp = str[i];
        str[i] = str[len-i-1];
        str[len-i-1] = temp;
    }
}

char* conv(char res[], int base, int inputNum)
{
    int index = 0;

    while (inputNum > 0)
    {
        res[index++] = reVal(inputNum % base);
        inputNum /= base;
    }
    res[index] = '\0';

    strev(res);

    return res;
}

void solve(){
    int n = 80;
    int m = 500;
    int s = uid(1, 10);
    cout << n << '\n';
    cout << m << '\n';
    cout << s << '\n';
    for(int i = 0; i < n; i++){
        int pid = i + 1;
        int start = uid(0, 10);
        int sz = uid(s, s + 30);
        int duration = sz;
        // address / (2 ^ 12) --> address / (2^12) < m

        cout << pid << ' ' << start << ' ' << duration << ' ' << sz << ' ';
        for(int k = 0; k < sz; k++){
            int ss = uid(4095, 4096 * m - 1);
            char result[10000];
            strcpy(result, conv(result, 16, ss));
            string hex = "";
            for(int j = 0; j < strlen(result); j++){
                hex += result[j];
            }
            while((int) hex.size() < 8){
                hex = '0' + hex;
            }
            cout << hex << (k == sz - 1 ? '\n' : ' ');
        }
    }
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(0);

    int tt = 1;
    //cin >> tt;
    outF

    while(tt--)
        solve();

    return 0;
}
