#include <iostream>
#include <cstdint>

using namespace std;

// Compte correctement le nombre de caractères (points de code) d'une chaîne encodée en UTF-8.
// compter les octets qui ne sont pas des continuations pour obtenir le nombre de caractères.
std::size_t strlength(std::string& str){
    std::size_t cpt = 0;
        
    for(uint8_t c : str){
    //on compare le bit de poids fort de c avec 128 (0x80)
        if((c& 192)!=128){
            ++cpt;
        } 
    }
    return cpt;
}

int main(){

    std::cout << "\n=== Exercice 1.1.1 ===\n";
     
    /*
        "See you soon!" est purement ASCII : 13 caracteres, 13 octets.
        .length() est donc correct.
        "À bientôt !" contient 11 caracteres visibles, mais À et ô
        sont chacun codés sur 2 octets en UTF-8, donc .length() vaut
        13 (11 + 2 octets supplementaires), et non 11.
    */
    
    std::string str1 = "See you soon!";
    std::string str2 = "À bientôt !";
    
    cout<<"Taille str1: "<<str1.length()<<endl;
    cout<<"Taille str2: "<<str2.length()<<endl;
    
    std::cout << "\n=== Exercice 1.1.2 ===\n";
    
    cout<<"Taille str1: "<<strlength(str1)<<endl;
    cout<<"Taille str2: "<<strlength(str2)<<endl;
}

