#include <iostream>
#include <string>
#include <vector>
 
using namespace std;

/*
* La fonction retourner(s) doit renvoyer une chaîne contenant tous les
* caractères de `s` dans l'ordre inverse. `s` est codée en UTF-8.
*
* Un std::reverse(s.begin(), s.end()) naïf inverserait les OCTETS un par un,
* ce qui cahngerait le codage en UTF-8 du caractère ('À' est codé su deux octets en UTF-8 (0xC3 0x80),
* les inverser donnerait 0x80 0xC3, ce qui est invalide puisque 0x80est un octet de continuation).
*
* On procède donc en deux étapes:
*
*   (1) Découper la chaîne en blocs correcpondant chacun à un caractère (1 à 4 octets selon le motif binaire du premier octet)
*   (2) Inverser l'ordre de ces blocs puis les recoller
*/

std::string retourner(const std::string& s){
    //On stocke chaque caractères comme une sous-chaîne
    //dans l'ordre où on les rencontre
    std::vector<std::string> caracteres;
    std::size_t i = 0;

    while(i < s.size()){
        unsigned char c = static_cast<unsigned char>(s[i]);
        std::size_t taille_octets;

        //On détermine la taille du caractère à partir des bits de poids fort du premier octet
        if      ((c & 0x80) == 0x00) taille_octets = 1; // 0xxxxxxx : ASCII (1 octet)
        else if ((c & 0xE0) == 0xC0) taille_octets = 2; // 110xxxxx : 2 octets
        else if ((c & 0xF0) == 0xE0) taille_octets = 3; // 1110xxxx : 3 octets
        else if ((c & 0xF8) == 0xF0) taille_octets = 4; // 11110xxx : 4 octets
        else                         taille_octets = 1; // octet invalide -> on avance quand même d'1 pour ne pas boucler indéfiniment
    
        //Si la chaine est mal formée en fin de buffer, on ne dépasse jamais sa taille réelle
        if (i + taille_octets > s.size()) taille_octets = s.size() - i;

        caracteres.push_back(s.substr(i, taille_octets));
        i += taille_octets;
    }

    //On reconstruit la chaine en parcourant les caractères du dernier au premier
    std::string resultat;
    for (auto it = caracteres.rbegin(); it!= caracteres.rend(); it++){
        resultat += *it; 
    }

    return resultat;
}

int main(){
    std::cout << "\n=== Exercice 1.3 ===\n";
 
    std::string s = "À bientôt !";
    std::cout << "s            = \"" << s << "\"\n";
    std::cout << "retourner(s) = \"" << retourner(s) << "\"\n";
    // Résultat attendu : "! tôtneib À"
}