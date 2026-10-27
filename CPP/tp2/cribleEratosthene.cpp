#include <iostream>
#include <stdint.h>

int main(void)
{
    const uint32_t N = 100;
    bool premier[N+1];
    
    // Initialisation: par défaut, tous les entiers sauf 0 et 1
    // sont considérés premiers.
    premier[0] = premier[1] = false;
    for (uint32_t i = 2; i <= N; ++i) {
        premier[i] = true;
    }
    
    // Criblage
    for (uint32_t i = 2; i <= N/2; ++i)
        if (premier[i]) // 'i' est premier: élimination des multiples
            for (uint32_t j = i*i; j <= N; j+=i)
    premier[j] = false;
                
    // Affichage
    for (uint32_t i = 2; i <= N; ++i)
         if (premier[i])
            std::cout << i << " ";
    std::cout << std::endl;
    
    return 0;
}
