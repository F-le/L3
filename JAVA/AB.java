public class AB {

    /*
     * Un arbre binaire est constitué :
     * - d'un élément (elt)
     * - d'un sous-arbre gauche (G)
     * - d'un sous-arbre droit (D)
     *
     * Si l'arbre est vide, on le représente par null.
     *
     * Les champs sont "protected" pour que ABR et AVL, qui héritent de AB, puissent y accéder.
     * Ils ne sont pas final car l'AVL modifie ses fils en place pendant les rotations.
     */
    protected int elt;
    protected AB G;
    protected AB D;


    /* 
     * Q1 — CONSTRUCTEUR
     *
     * Le constructeur permet de créer un nœud avec :
     * - une valeur elt
     * - un sous-arbre gauche G
     * - un sous-arbre droit D
     *
     * AB a = new AB(10, null, null);
     * crée :
     *
     *       10
     *      /  \
     *    vide vide
     */
    public AB(int elt, AB G, AB D) {
        this.elt = elt;
        this.G = G;
        this.D = D;
    }


    /*
     * ARBRE VIDE
     *
     * L'arbre vide est représenté par la valeur null. On retourne donc simplement null.
     */
    public static AB arbre_vide() {
        return null;
    }


    /* 
     * SOUS-ARBRE GAUCHE
     *
     * Retourne le sous-arbre gauche du nœud courant.
     *
     *       10
     *      /  \
     *     5   15
     *
     * gauche() retourne l'arbre dont la racine est 5.
     */
    public AB gauche() {
        return this.G;
    }


    /* 
     * SOUS-ARBRE DROIT
     * 
     * Retourne le sous-arbre droit du nœud courant.
     *
     *       10
     *      /  \
     *     5   15
     *
     * droite() retourne l'arbre dont la racine est 15.
     */
    public AB droite() {
        return this.D;
    }


    /*
     * CONSTRUCTION D'UN ARBRE — CONS
     *
     * Cette méthode crée un nouvel arbre dont :
     * - la racine contient elt
     * - le sous-arbre gauche est g
     * - le sous-arbre droit est d
     *
     * AB a = cons(10, gauche, droite);
     * donne :
     *
     *          10
     *         /  \
     *     gauche droite
     */
    public static AB cons(int elt, AB g, AB d) {
        return new AB(elt, g, d);
    }


    /*
     * ELT
     *
     * Retourne l'élément situé à la racine de l'arbre courant. (On suppose que l'arbre n'est pas vide.)
     */
    public int elt() {
        return this.elt;
    }


    /*
     * VIDE
     *
     * Permet de savoir si un arbre est vide.
     * Comme un arbre vide est représenté par null :
     *
     *     ab == null  --> arbre vide
     *     ab != null  --> arbre non vide
     */
    public static boolean vide(AB ab) {
        return ab == null;
    }


    /*
     * Q2 — PARCOURS PRÉFIXE
     *
     * Dans un parcours préfixe, on visite :
     *
     * RACINE -> GAUCHE -> DROITE (P(A) = r.P(A1)...P(Ak))
     *
     *          1
     *         / \
     *        2   3
     *
     * Le parcours préfixe affiche: 1 2 3
     */
    public void afficherPrefixe(AB ab) {

        /*
         * Condition d'arrêt :
         *
         * Si ab == null, cela signifie que nous sommes arrivés
         * sur un sous-arbre vide.
         *
         * Il ne faut alors rien faire et surtout ne pas essayer
         * d'accéder à ab.elt, car cela provoquerait un
         * NullPointerException.
         */
        if (!vide(ab)) {

            //On affiche d'abord la racine. C'est ce qui donne le caractère "préfixe" au parcours.
            System.out.print(ab.elt + " ");

            //On parcourt ensuite récursivement le sous-arbre gauche.
            afficherPrefixe(ab.G);

            //Enfin, on parcourt récursivement le sous-arbre droit.
            afficherPrefixe(ab.D);
        }
    }


    /*
     * Q3 — RECHERCHE
     *
     * L'opération de recherche retourne :
     *
     * - le sous-arbre dont la racine contient la valeur recherchée
     * - ou un arbre vide (null) si la valeur n'existe pas.
     *
     * Nous devons potentiellement explorer les deux sous-arbres.
     */
    public AB rechercher(int elt) {

        /*
         * CAS 1 : la racine contient la valeur recherchée
         *
         * Si la valeur du nœud courant est celle recherchée, nous avons trouvé le bon sous-arbre.
         * On ne retourne pas seulement l'entier elt, mais l'objet AB correspondant au sous-arbre (donc un 'return this')
         *
         *          10
         *         /  \
         *        5   15
         *
         * rechercher(5) doit retourner : 5
         * c'est-à-dire le sous-arbre ayant 5 pour racine.
         */
        if (this.elt == elt) {
            return this;
        }


        /*
         * CAS 2 : rechercher dans le sous-arbre gauche
         *
         * On commence par chercher à gauche.
         * si G est null, appeler rechercher directement sur G serait impossible.
         * On teste donc d'abord que G n'est pas null.
         */
        if (G != null) {

            AB resultat = G.rechercher(elt);

            /*
             * Si resultat != null, cela signifie que la valeur a été trouvée dans le sous-arbre gauche.
             * On peut donc immédiatement retourner le résultat.
             */
            if (resultat != null) {
                return resultat;
            }
        }


        /*
         * CAS 3 : rechercher dans le sous-arbre droit
         *
         * Si la valeur n'a pas été trouvée à gauche, on cherche maintenant dans le sous-arbre droit.
         */
        if (D != null) {

            AB resultat = D.rechercher(elt);

            //Si la recherche réussit, on retourne le sous-arbre trouvé.
            if (resultat != null) {
                return resultat;
            }
        }


        /*
         * CAS 4 : valeur absente
         *
         * Si nous arrivons ici, cela signifie que :
         *
         * - la racine n'est pas elt
         * - elt n'est pas dans le sous-arbre gauche
         * - elt n'est pas dans le sous-arbre droit
         *
         * La valeur n'existe donc pas dans l'arbre, l'arbre vide est représenté par null.
         */
        return null;
    }
}