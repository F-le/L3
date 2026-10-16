public class ABR extends AB {

    /*
     * Un arbre binaire de recherche est constitué d' :
     * - une valeur entière elt
     * - un sous-arbre gauche G
     * - un sous-arbre droit D
     *
     * elt, G et D sont hérités de AB (champs protected).
     */

    /*
     * Constructeur d'un nœud.
     *
     * valeur stockée dans le nœud
     * sous-arbre gauche
     * sous-arbre droit
     */
    public ABR(int elt, ABR G, ABR D) {
        super(elt, G, D);
    }

    // Q1

    /*
     * Crée/représente un arbre vide.
     *
     * Ici, comme un arbre vide est représenté par null,
     * la méthode retourne simplement null.
     */
    public static ABR arbre_vide() {
        return null;
    }

    //Retourne le sous-arbre gauche du nœud courant.
    @Override
    public ABR gauche() {
        return (ABR) this.G;
    }

    //Retourne le sous-arbre droit du nœud courant.
    @Override
    public ABR droite() {
        return (ABR) this.D;
    }

    /*
     * Construit un nouvel ABR avec :
     * - elt comme racine
     * - g comme sous-arbre gauche
     * - d comme sous-arbre droit
     */
    public static ABR cons(int elt, ABR g, ABR d) {
        return new ABR(elt, g, d);
    }

    //Retourne la valeur stockée dans la racine.
    @Override
    public int elt() {
        return this.elt;
    }

    /*
     * Permet de tester si un arbre est vide.
     *
     * On compare simplement avec null car null représente un arbre vide.
     */
    public static boolean vide(ABR a) {
        return a == null;
    }

    /*
     * 2. RECHERCHE DANS UN ABR
     *
     * Recherche la valeur x dans l'ABR a.
     *
     * Retour :
     * - le sous-arbre dont la racine contient x si x est trouvé
     * - null sinon
     *
     * Un ABR respecte la règle :
     *     valeurs du sous-arbre gauche < racine
     *     valeurs du sous-arbre droit > racine
     *
     * Donc :
     * - si x < racine, x ne peut être qu'à gauche
     * - si x > racine, x ne peut être qu'à droite
     * - si x == racine, on a trouvé
     */
    public static ABR rechercher(ABR a, int x) {

        //B représente le sous-arbre dans lequel on cherche. On commence avec l'arbre complet.
        ABR B = a;

        /*
         * On continue tant que :
         * - B n'est pas vide
         * - x n'est pas encore trouvé à la racine
         */
        while (B != null && x != B.elt) {

            //x est plus petit que la racine : on doit aller dans le sous-arbre gauche.
            if (x < B.elt) {
                B = B.gauche();
            }

            //Sinon x est plus grand que la racine : on va dans le sous-arbre droit.
            else {
                B = B.droite();
            }
        }

        /*
         * Deux possibilités :
         *
         * B != null  --> x a été trouvé
         * B == null  --> x n'existe pas dans l'arbre
         */
        return B;
    }

    /*
     * Redéfinition de AB.rechercher(int) pour un ABR.
     *
     * AB.rechercher explore potentiellement tout l'arbre (O(n)).
     * Ici, grâce à la propriété d'ABR, on réutilise la recherche
     * qui ne descend que dans un seul sous-arbre (O(h)).
     */
    @Override
    public ABR rechercher(int x) {
        return rechercher(this, x);
    }

    /*
     * 3. AJOUT D'UNE VALEUR
     *
     * Ajoute x dans l'ABR et retourne le nouvel arbre.
     *
     * - arbre vide : on crée un nouveau nœud
     * - x < racine : on ajoute x dans le sous-arbre gauche
     * - x > racine : on ajoute x dans le sous-arbre droit
     * - x == racine : on ne fait rien car la valeur est déjà présente.
     * 
     * O(h(A)).
     */
    public static ABR ajouter(ABR a, int x) {

        /*
         * Cas de base : si l'endroit où l'on veut insérer est vide, on crée un nouveau nœud.
         * Le nouveau nœud est une feuille : ses deux sous-arbres sont donc vides.
         */
        if (a == null) {
            return cons(x, null, null);
        }

        /*
         * x est plus petit que la racine : on doit l'insérer dans le sous-arbre gauche.
         * On reconstruit ensuite l'arbre avec la même racine.
         */
        if (x < a.elt) {
            return cons(a.elt, ajouter(a.gauche(), x), a.droite());
        }

        // x est plus grand que la racine : on l'insère dans le sous-arbre droit.
        if (x > a.elt) {
            return cons(a.elt, a.gauche(), ajouter(a.droite(), x));
        }

        //Ici x == a.elt. La valeur est déjà dans l'arbre. On ne crée donc pas de doublon.
        return a;
    }


    //Q2

    /*
     * 4. MAXIMUM D'UN ABR
     *
     * Dans un ABR, la plus grande valeur se trouve toujours complètement à droite.
     *
     *             5
     *           /   \
     *          3     8
     *               / \
     *              7   10
     *
     * Le maximum est 10.
     *
     * Pour trouver le maximum, il suffit donc de suivre les pointeurs droits jusqu'à atteindre un nœud qui n'a plus de sous-arbre droit.
     *
     * O(h).
     */
    public int MAX() {

        //Cas d'arrêt : s'il n'y a plus de sous-arbre droit, le nœud courant contient le maximum.
        if (this.D == null) {
            return this.elt;
        }

        // Sinon, le maximum est nécessairement dans D.
        return this.droite().MAX();
    }

    //Q3

    /*
     * 5. SUPPRESSION
     *
     * Supprime x de l'ABR a et retourne le nouvel arbre.
     *
     * Il faut distinguer plusieurs cas.
     *
     * CAS 1 : x < racine -> x est forcément dans G
     * CAS 2 : x > racine -> x est forcément dans D
     * CAS 3 : x == racine -> on a trouvé le nœud à supprimer.
     *
     * Pour le troisième cas, il existe encore trois situations :
     *
     * 1) pas de sous-arbre gauche -> on retourne D
     * 2) pas de sous-arbre droit -> on retourne G
     * 3) les deux sous-arbres existent -> on remplace la valeur de la racine par le maximum du sous-arbre gauche, puis on supprime ce maximum du sous-arbre gauche.
     */
    public static ABR supprimer(ABR a, int x) {

        // Si l'arbre est vide, x n'est pas présent. On retourne donc l'arbre vide.
        if (a == null) {
            return null;
        }

        // x est plus petit que la racine : la suppression doit se faire dans G. La racine et D restent inchangés.
        if (x < a.elt) {
            return cons(a.elt, supprimer(a.gauche(), x), a.droite());
        }

        //x est plus grand que la racine : la suppression doit se faire dans D.
        if (x > a.elt) {
            return cons(a.elt, a.gauche(), supprimer(a.droite(), x));
        }

        //Ici x == a.elt: Nous avons donc trouvé le nœud à supprimer.

        /*
         * CAS 1 : Le sous-arbre gauche est vide.
         *
         * On peut simplement remplacer le nœud courant par son sous-arbre droit.
         *
         *       5
         *        \
         *         8
         * Après suppression de 5 : 8
         */
        if (a.gauche() == null) {
            return a.droite();
        }

        /*
         * CAS 2 : Le sous-arbre droit est vide.
         *
         * On remplace alors le nœud par son sous-arbre gauche.
         *
         *       5
         *      /
         *     3
         * Après suppression de 5 : 3
         */
        if (a.droite() == null) {
            return a.gauche();
        }

        /*
         * CAS 3 : Les deux sous-arbres existent.
         *
         * On choisit le maximum du sous-arbre gauche.
         * 
         * Parce que ce maximum est :
         * - plus grand que tous les autres éléments de G
         * - toujours plus petit que les éléments de D
         *
         * Il peut donc remplacer correctement la racine sans casser la propriété d'ABR.
         */
        int maximumGauche = a.gauche().MAX();

        /*
         * On reconstruit l'arbre : nouvelle racine = maximum du sous-arbre gauche
         * nouveau sous-arbre gauche = ancien G auquel on a retiré son maximum
         * sous-arbre droit = ancien D
         */
        return cons(maximumGauche, supprMax(a.gauche()), a.droite());
    }

    /*
     * 6. SUPPRIMER LE MAXIMUM D'UN ABR
     *
     * - si D est vide, le nœud courant est le maximum ; on le remplace alors par G.
     * - sinon, le maximum est dans D et on continue,récursivement.
     */
    private static ABR supprMax(ABR a) {

        //Le maximum est atteint : aucun fils droit. Le sous-arbre gauche doit prendre la place du nœud.
        if (a.droite() == null) {
            return a.gauche();
        }

        /*
         * Sinon le maximum est plus loin à droite.
         *
         * On reconstruit le nœud courant avec :
         * - la même valeur
         * - le même sous-arbre gauche
         * - le sous-arbre droit auquel on a retiré le maximum
         */
        return cons(a.elt, a.gauche(), supprMax(a.droite()));
    }
}