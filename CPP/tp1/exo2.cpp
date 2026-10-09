#include <iostream>
#include <string>
#include <cstdint>
#include <algorithm>
#include <stdexcept>

using namespace std;

//fonction qui donne une valeur à un caractère
/*
0-9 = 0-9
a-z= 10-35
*/
int digit_to_value(char c){
    if(c>='0' && c<='9') return c-'0';
    if(c>='a' && c<='z') return c - 'a' + 10;
    if(c>='A' && c<='Z') return c - 'A' + 10;
    throw std::invalid_argument("caractere invalide");
}

//fonction qui convertit un nombre en symbole
/*
0-9 = 0-9
10-35= a-z
*/
char value_to_digit(int v){
    if(v>=0 && v<=9) return static_cast<char> ('0' + v);
    if(v>=10 && v<=35) return static_cast<char> ('a'+(v-10));
    throw std::invalid_argument("valeur de chiffre invalide");
}

//fonction retournant une chaîne de caractères correspondant à la chaîne initiale 
//exprimée dans la base d'arrivée
/*
base_converter("3eh12",18,30) -> "eqne"
*/
std::string base_converter(std::string str, int base_depart, int base_arrivee){
    //on s'assure que les bases sont comprises entre 2 et 36 (0-9; a-z)
    if(base_depart<2 || base_depart >36 || base_arrivee<2 || base_arrivee>36){
        return "Les bases doivent être comprises entre 2 et 36!";
    }
    
    //on s'assure que la chaine de caractère n'est pas vide
    if(str.empty()){
        return "Chaîne vide";
    }
    
    //chaine à valeur entière
    //long long plutôt que int pour limiter les dépassements sur de longues chaines
    long long value = 0;
    
    for(char c:str){
        int d = digit_to_value(c);
        if (d>=base_depart){
            return "Chiffre invalide pour la base de depart!";
        }
        value = value*base_depart+d;
    }
    
    if (value == 0) return "0";
    
    //valeur entière à chaine base d'arrivée
    std::string resultat;
    
    while(value>0){
        int reste = static_cast<int>(value%base_arrivee);
        resultat.push_back(value_to_digit(reste));
        value /= base_arrivee;
    }
    
    std::reverse(resultat.begin(), resultat.end());
    return resultat;
}

int main(){
    std::cout << "\n=== Exercice 1.2 ===\n";
    
    std::string r = base_converter("3eh12", 18, 30);
    std::cout << "base_converter(\"3eh12\", 18, 30) = \"" << r << "\""<< " (attendu : \"eqne\")\n";
}
