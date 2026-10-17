#include <iostream>
#include <cstdint>

using namespace std;

int main(){
	int32_t n;
	int32_t nb_coups;
	int32_t a_decouvrir = rand() % 101;

	while(n!=a_decouvrir){
        cout<<"Entrer un entire positif: ";
	    cin>>n;

		if(n<a_decouvrir){
			cout<<"n est plus petit que le nombre à découvrir"<<endl;
		} else{
			cout<<"n est plus grand que le nombre à découvrir"<<endl;
		}
		nb_coups++;
	}

    cout<<"Nombre de coups: "<<nb_coups<<endl;
}