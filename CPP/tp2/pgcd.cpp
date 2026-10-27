#include <iostream>
#include <string>
#include <cassert>
int pgcd( int a, int b){
    assert(a>=b);
    if (b==0){
        return a;    
    } else{
        return pgcd(b, a%b);    
    }
}
int main()
{
    std::cout << pgcd(48, 18) << std::endl;   // attendu : 6
}
