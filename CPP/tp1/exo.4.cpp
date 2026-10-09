#include <iostream>
#include <string>
#include <variant>
#include <cstdint>
 
using namespace std;

/*
* Le statut de la personne détermine quel champ de l'union est valide
* code sur 1 octet (uint8_t)
*/
enum class Statut : uint8_t {ETUDIANT, EMPLOYE, RETRAITE};

/*
* Un type est "POD" (Plain Old Data) s'il ne contient que des types simples (entiers,
* flottants, tableaux de taille fixe, union, autres PODs...), sans
* constructeur/destructeur personnalisé. Un POD peut être copié octet à
* octet avec memcpy(), écrit tel quel dans un fichier binaire, envoyé sur
* le réseau sous forme brute, etc. C'est pour ça qu'on utilise des
* tableaux de char de taille FIXE plutôt que des std::string ici.
*/
struct pod_personne_t{
    char nom [30];
    char prenom [10];
    uint8_t age;                        // entier non signé sur 1 octet (0 à 255, largement suffisant pour un âge)
    double poids;
    Statut statut;

    //Un seul des 3 champs est actif à la fois selon la valeur de statut.
    // On regroupe dans une union pour économiser de la mémoire
    union{
        char employeur [30];                //si statut == EMPLOYE
        uint16_t id_ecole;                  //si statut == ETUDIANT
        uint16_t annee_retraite;            //si statut == RETRAITE
    };
};








/*
* Remplacer les tableaux de taille fixe par des std::string pose une
* difficulté pour la partie "union" : on ne peut PAS mettre un std::string
* dans une union C++ classique (ses membres doivent avoir des
* constructeurs/destructeurs triviaux, ce que std::string n'a pas).
* La solution moderne est std::variant, qui est une "union sûre" gérant
* correctement la construction/destruction de types non triviaux.
*
* Un std::variant ne peut pas avoir deux alternatives du
* MÊME type (ici on aurait voulu uint16_t pour id_ecole ET pour
* annee_retraite) car std::get<uint16_t> serait alors ambigu : on ne
* saurait pas lequel des deux on récupère. On introduit donc deux petits
* types "étiquettes" distincts, juste pour lever cette ambiguïté.
*/
struct IdEcole       { uint16_t valeur; };
struct AnneeRetraite { uint16_t valeur; };
 
struct personne_t {
    std::string nom;
    std::string prenom;
    uint8_t age;
    double poids;
    Statut statut;
    std::variant<std::string, IdEcole, AnneeRetraite> info;
};

int main(){
    std::cout << "\n=== Exercice 1.4 ===\n";
 
    std::cout << "sizeof(pod_personne_t) = " << sizeof(pod_personne_t) << " octets\n";
    std::cout << "sizeof(personne_t)     = " << sizeof(personne_t)     << " octets\n";
 
    // Somme "naïve" des tailles des champs de pod_personne_t, pour
    // comparer avec le sizeof() réel (l'union compte pour la taille de
    // son plus grand membre, soit 30 octets pour employeur[30]) :
    std::size_t somme_naive =
        sizeof(char[30]) + sizeof(char[10]) + sizeof(uint8_t) +
        sizeof(double) + sizeof(Statut) + sizeof(char[30]);
    std::cout << "Somme naive des membres de pod_personne_t = " << somme_naive << " octets\n";
}





    // EXPLICATION DE LA DIFFERENCE (padding/alignement) :
    //
    // sizeof(pod_personne_t) est généralement SUPERIEUR à la somme naïve,
    // à cause des octets de remplissage ("padding") que le compilateur
    // insère automatiquement entre certains champs. Sur la plupart des
    // architectures, un type de N octets doit commencer à une adresse
    // mémoire multiple de N (ou d'un alignement propre à N) : un `double`
    // (8 octets) doit démarrer à une adresse multiple de 8, un `uint16_t`
    // à une adresse multiple de 2, etc. Si le champ précédent ne tombe
    // pas "pile" sur la bonne adresse, le compilateur ajoute des octets
    // invisibles avant le champ suivant pour respecter cette contrainte.
    // La taille totale de la structure est elle-même arrondie à un
    // multiple de l'alignement le plus strict parmi tous ses membres
    // (ici 8, à cause du double), pour que des tableaux de cette
    // structure restent correctement alignés.
    //
    // EXPLICATION SUR personne_t (plus POD) :
    //
    // personne_t n'est PLUS un type POD : std::string et std::variant ont
    // des constructeurs/destructeurs non triviaux et gèrent de la mémoire
    // allouée dynamiquement sur le tas. On ne peut donc plus faire de
    // memcpy() dessus, ni l'écrire tel quel dans un fichier binaire — il
    // faudrait sérialiser "à la main" le contenu réel des chaînes.
    // sizeof(personne_t) reste une valeur FIXE et connue à la compilation
    // (elle ne compte que les pointeurs/tailles/capacités internes de
    // chaque std::string — typiquement ~32 octets chacune avec libstdc++
    // sur une machine 64 bits — plus la taille du variant), mais la
    // mémoire RÉELLEMENT consommée par un objet personne_t varie à
    // l'exécution selon la longueur effective des chaînes stockées (une
    // optimisation appelée SSO, Small String Optimization, évite même
    // l'allocation sur le tas pour les très courtes chaînes). Le sizeof()
    // ne reflète donc plus toute la mémoire utilisée par l'objet.