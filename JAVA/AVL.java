public class AVL extends ABR {

    /*
     * Un AVL est avant tout un ABR : valeurs gauche < racine < valeurs droite
     * MAIS il doit également être équilibré : | h(D) - h(G) | <= 1
     *
     * On stocke donc :
     * - elt : la valeur du nœud
     * - G   : sous-arbre gauche
     * - D   : sous-arbre droit
     * - bal : balance du nœud
     *
     * La balance est définie par : bal = h(D) - h(G)
     *
     * Un AVL normal possède donc des balances -1, 0 ou +1.
     * Pendant une opération, une balance de -2 ou +2 peut apparaître temporairement : c'est à ce moment qu'on effectue une rotation.
     */

    /*
     * elt, G et D sont hérités de AB (via ABR) : champs protected.
     * Seule la balance est propre à l'AVL.
     */
    private int bal;

    // CONSTRUCTEUR 

    /*
    * Au départ, la balance doit correspondre à la hauteur des deux sous-arbres.
    * Dans les opérations Ajouter/Supprimer, on modifie directement bal afin d'éviter de recalculer les hauteurs par parcours.
    */
    public AVL(int elt, AVL G, AVL D) {
        super(elt, G, D);
        this.bal = 0;
    }

    /*
     * Une feuille a :
     *     h(G) = 0
     *     h(D) = 0
     *
     * donc : bal = 0
     */
    public AVL(int elt) {
        super(elt, null, null);
        this.bal = 0;
    }

    //OPERATIONS DE BASE

    //Teste si un AVL est vide.
    public static boolean vide(AVL a) {
        return a == null;
    }

    //Retourne la valeur de la racine.
    @Override
    public int elt() {
        return this.elt;
    }

    //Retourne le sous-arbre gauche.
    @Override
    public AVL gauche() {
        return (AVL) this.G;
    }

    //Retourne le sous-arbre droit.
    @Override
    public AVL droite() {
        return (AVL) this.D;
    }

    //Retourne la balance du nœud.
    public int balance() {
        return this.bal;
    }

    /*
     * RESULTAT D'UNE OPERATION RECURSIVE
     *
     * Ajouter et Supprimer ne retournent pas uniquement le nouvel arbre : 
     * elles retournent également une information sur la variation de hauteur du sous-arbre.
     *
     * Cela permet de remonter dans l'arbre sans recalculer les hauteurs par parcours.
     *
     * Pour Ajouter : variation = 0 ou +1
     * Pour Supprimer : variation = 0 ou -1
     */
    private static class Resultat {
        AVL arbre;
        int deltaHauteur;

        Resultat(AVL arbre, int deltaHauteur) {
            this.arbre = arbre;
            this.deltaHauteur = deltaHauteur;
        }
    }

    /*
     * RECHERCHE
     *
     * Recherche x dans l'AVL.
     *
     * Comme un AVL est un ABR, on utilise exactement le principe de recherche d'un ABR :
     *     x < racine  --> gauche
     *     x > racine  --> droite
     *     x = racine  --> trouvé
     *
     * L'AVL ayant une hauteur en O(log n), la recherche est donc en O(log n) au pire.
     */
    public static AVL rechercher(AVL a, int x) {

        // Un AVL est un ABR : on réutilise la recherche de ABR. Les nœuds d'un AVL étant tous des AVL, le cast est sûr.
        return (AVL) ABR.rechercher(a, x);
    }

    // Version d'instance (redéfinit ABR.rechercher(int)).
    @Override
    public AVL rechercher(int x) {
        return rechercher(this, x);
    }

    /*
     * ROTATION GAUCHE
     *
     * Cas général :
     *
     *          A                         B
     *         / \                       / \
     *        Ag  B        --->         A   Bd
     *           / \                   / \
     *          Bg  Bd                Ag Bg
     *
     * On fait monter B et descendre A. La rotation conserve l'ordre de l'ABR.
     * 
     * O(1)
     */
    private static AVL ROTG(AVL A) {

        //B est le fils droit de A.  Une rotation gauche n'est possible que si B existe.
        AVL B = A.droite();

        // On conserve les anciennes balances car elles sont nécessaires dans les formules du cours.
        int a = A.bal;
        int b = B.bal;

        /*
         * Réorganisation des pointeurs :
         *
         *       A               B
         *        \             / \
         *         B    -->    A   ...
         *        /
         *       Bg
         *
         * Le sous-arbre gauche de B devient le sous-arbre droit de A.
         */
        A.D = B.gauche();
        B.G = A;

        //Mise à jour de la balance de A.
        A.bal = a - Math.max(b, 0) - 1;

        // Mise à jour de la balance de B.
        B.bal = Math.min(a - 2, Math.min(a + b - 2, b - 1));

        // B devient la nouvelle racine du sous-arbre.
        return B;
    }

    /*
     * ROTATION DROITE
     *
     * Symétrique de la rotation gauche.
     *
     *          A                         B
     *         / \                       / \
     *        B   Ad       --->         Bg  A
     *       / \                           / \
     *      Bg Bd                         Bd Ad
     */
    private static AVL ROTD(AVL A) {

        //B est le fils gauche de A.
        AVL B = A.gauche();

        //Anciennes balances.
        int a = A.bal;
        int b = B.bal;

        /*
         * Réorganisation des pointeurs.
         * Le sous-arbre droit de B devient le sous-arbre gauche de A.
         */
        A.G = B.droite();
        B.D = A;

        //Mise à jour de la balance de A.
        A.bal = a - Math.min(b, 0) + 1;

        //Mise à jour de la balance de B.
        B.bal = Math.max(a + 2, Math.max(a + b + 2, b + 1));

        //B devient la nouvelle racine du sous-arbre.
        return B;
    }

    /*
     * DOUBLE ROTATION GAUCHE
     *
     * Cas où le déséquilibre est à droite mais que le fils droitpenche vers la gauche.
     *
     * On réalise :
     *       ROTD(sous-arbre droit)
     *       puis
     *       ROTG(racine)
     */
    private static AVL DROTG(AVL A) {

        // On commence par une rotation droite sur le sous-arbre droit.
        A.D = ROTD(A.droite());

        //Puis on effectue une rotation gauche sur A.
        return ROTG(A);
    }


    /*
     * DOUBLE ROTATION DROITE
     *
     * Symétriquement :
     *
     *       ROTG(sous-arbre gauche)
     *       puis
     *       ROTD(racine)
     */
    private static AVL ROTGD(AVL A) {

        //Rotation gauche sur le sous-arbre gauche.
        A.G = ROTG(A.gauche());

        //Puis rotation droite sur A.
        return ROTD(A);
    }

    /*
     * EQUILIBRAGE
     *
     * Cette fonction reçoit un arbre dont :
     * - les deux sous-arbres sont déjà des AVL
     * - la balance de la racine peut temporairement valoir
     * 
     *   -2, -1, 0, +1 ou +2
     *
     * Son objectif est de remettre la balance dans [-1 ; +1].
     *
     * Il existe 4 cas :
     *     +2 avec fils droit >= 0  --> rotation gauche
     *     +2 avec fils droit <  0  --> double rotation gauche
     *
     *     -2 avec fils gauche <= 0 --> rotation droite
     *     -2 avec fils gauche >  0 --> double rotation droite
     */
    private static AVL equilibrer(AVL A) {

        // CAS 1 ET 2 : déséquilibre vers la droite -> bal(A) = +2
        if (A.bal == 2) {

            // Le fils droit penche lui-même à droite (ou est équilibré). Une seule rotation gauche suffit.
            if (A.droite().bal >= 0) {
                return ROTG(A);
            }

            /*
             * Le fils droit penche à gauche.
             * Il faut donc faire :
             *     ROTD(D)
             *     puis ROTG(A)
             */
            else {
                A.D = ROTD(A.droite());
                return ROTG(A);
            }
        }

        //CAS 3 ET 4 : déséquilibre vers la gauche -> bal(A) = -2
        if (A.bal == -2) {

            // Le fils gauche penche lui-même à gauche (ou est équilibré). Une seule rotation droite suffit.
            if (A.gauche().bal <= 0) {
                return ROTD(A);
            }

            /*
             * Le fils gauche penche à droite.
             *
             * Il faut donc :
             *     ROTG(G)
             *     puis ROTD(A)
             */
            else {
                A.G = ROTG(A.gauche());
                return ROTD(A);
            }
        }

        // Si la balance vaut -1, 0 ou +1, l'arbre est déjà équilibré.
        return A;
    }

    /*
     * AJOUT D'UN ELEMENT
     *
     * La fonction retourne : (nouvel arbre, variation de hauteur)
     *
     * La variation vaut :
     *     0  : la hauteur n'a pas augmenté
     *     +1 : la hauteur a augmenté de 1
     *
     * Pourquoi propager cette information ?
     *
     * Parce qu'un changement de hauteur peut modifier la balance du parent. On remonte donc l'information 
     * seulement sur le chemin parcouru, sans recalculer les hauteurs de tous les nœuds.
     */
    private static Resultat ajouterRec(AVL A, int x) {

        // CAS DE BASE : arbre vide On crée une nouvelle feuille -> Sa balance est 0. La hauteur augmente de 1.
        if (A == null) {
            return new Resultat(new AVL(x), 1);
        }

        /*
         * x est déjà présent
         * Dans un AVL, on n'ajoute jamais deux fois le même élément. La structure et la hauteur ne changent donc pas.
         */
        if (x == A.elt) {
            return new Resultat(A, 0);
        }

        // AJOUT DANS LE SOUS-ARBRE DROIT
        if (x > A.elt) {

            // On ajoute récursivement dans D.
            Resultat r = ajouterRec(A.droite(), x);

            //Le fils droit devient le nouvel arbre obtenu.
            A.D = r.arbre;

            // Comme le sous-arbre droit a varié de h, la balance de A varie également de +h.
            int h = r.deltaHauteur;

            // Si le sous-arbre droit n'a pas changé de hauteur, la hauteur de A ne change pas non plus.
            if (h == 0) {
                return new Resultat(A, 0);
            }

            /*
             * La balance est : h(D) - h(G)
             * Donc une augmentation de D de h donne : bal = bal + h
             */
            A.bal = A.bal + h;
        }

        // AJOUT DANS LE SOUS-ARBRE GAUCHE
        else {

            // On ajoute récursivement dans G.
            Resultat r = ajouterRec(A.gauche(), x);

            // Nouveau sous-arbre gauche.
            A.G = r.arbre;

            /*
             * Une augmentation de la hauteur gauche diminue la balance : bal = h(D) - h(G)
             * donc : bal = bal - h
             * On utilise donc -h.
             */
            int h = -r.deltaHauteur;

            // Aucun changement de hauteur.
            if (h == 0) {
                return new Resultat(A, 0);
            }

            // Mise à jour de la balance.
            A.bal = A.bal + h;
        }

        // REEQUILIBRAGE
        A = equilibrer(A);

        /*
         * Après équilibrage :
         * - si la balance vaut 0, la hauteur du sous-arbre est revenue à sa hauteur initiale ;
         * - sinon la hauteur a augmenté de 1.
         */
        if (A.bal == 0) {
            return new Resultat(A, 0);
        }

        return new Resultat(A, 1);
    }

    /*
     * Méthode publique d'ajout.
     * Elle masque le mécanisme interne de propagation de la variation de hauteur.
     */
    public static AVL ajouter(AVL A, int x) {

        Resultat r = ajouterRec(A, x);

        // On ne retourne à l'utilisateur que l'arbre.
        return r.arbre;
    }

    // MINIMUM : Le minimum d'un ABR/AVL se trouve toujours complètement à gauche.
    private static int MIN(AVL A) {

        AVL courant = A;

        // On descend toujours à gauche.
        while (courant.gauche() != null) {
            courant = courant.gauche();
        }

        return courant.elt;
    }

    /*
     * SUPPRESSION DU MINIMUM
     *
     * On utilise Suppr_min dans la suppression lorsqu'un nœud possède deux enfants.
     * La fonction retourne : (nouvel arbre, variation de hauteur) avec une variation de 0 ou -1.
     */
    private static Resultat supprMin(AVL A) {

        /*
         * Si le fils gauche est vide, A est le minimum.
         * Pour retirer A, il suffit de le remplacer par son sous-arbre droit.
         * La hauteur diminue alors de 1.
         */
        if (A.gauche() == null) {
            return new Resultat(A.droite(), -1);
        }

        // Sinon le minimum est plus bas dans G.
        Resultat r = supprMin(A.gauche());

        // Le sous-arbre gauche devient le résultat.
        A.G = r.arbre;

        /*
         * Comme on a supprimé un élément dans G, une diminution de sa hauteur augmente la balance : bal = h(D) - h(G)
         * Si h(G) diminue de 1 : bal = bal + 1
         */
        int h = -r.deltaHauteur;

        // Si la hauteur de G n'a pas changé, celle de A ne change pas.
        if (h == 0) {
            return new Resultat(A, 0);
        }

        // Mise à jour de la balance.
        A.bal = A.bal + h;

        // L'arbre peut maintenant être déséquilibré.
        A = equilibrer(A);

        /*
         * Après équilibrage : bal == 0 : la hauteur a diminué de 1
         * sinon : a hauteur reste inchangée
         */
        if (A.bal == 0) {
            return new Resultat(A, -1);
        }

        return new Resultat(A, 0);
    }

    /*
     * SUPPRESSION D'UN ELEMENT
     *
     * Retourne le nouvel AVL après suppression de x.
     *
     * Comme dans un ABR, il faut distinguer :
     *
     * 1. x < racine  --> chercher à gauche
     * 2. x > racine  --> chercher à droite
     * 3. x == racine --> on a trouvé le nœud.
     *
     * Lorsqu'on trouve le nœud, trois cas existent :
     *
     * A) pas de sous-arbre gauche
     * B) pas de sous-arbre droit
     * C) deux sous-arbres
     *
     * Dans le cas C, on remplace l'élément par MIN(D), puis on supprime ce minimum.
     */
    private static Resultat supprimerRec(AVL A, int x) {

        // ARBRE VIDE: Rien à supprimer. La hauteur ne change pas.
        if (A == null) {
            return new Resultat(null, 0);
        }

        // RECHERCHE DANS LE SOUS-ARBRE DROIT
        if (x > A.elt) {

            // Suppression récursive dans D.
            Resultat r = supprimerRec(A.droite(), x);

            // Mise à jour du fils droit.
            A.D = r.arbre;

            // La variation de hauteur de D peut modifier directement la balance de A.
            int h = r.deltaHauteur;

            // Si la hauteur de D n'a pas changé, celle de A ne change pas non plus.
            if (h == 0) {
                return new Resultat(A, 0);
            }

            /*
             * D a diminué de hauteur : la balance diminue.
             * Ici r.deltaHauteur vaut -1.
             */
            A.bal = A.bal + h;
        }

        // RECHERCHE DANS LE SOUS-ARBRE GAUCHE
        else if (x < A.elt) {

            // Suppression récursive dans G.
            Resultat r = supprimerRec(A.gauche(), x);

            // Mise à jour du fils gauche.
            A.G = r.arbre;

            // Une baisse de hauteur dans G augmente la balance.
            int h = -r.deltaHauteur;

            // Aucun changement de hauteur.
            if (h == 0) {
                return new Resultat(A, 0);
            }

            // Mise à jour de la balance.
            A.bal = A.bal + h;
        }

        // x == A.elt : ON A TROUVE LE NŒUD
        else {

            /*
             * CAS 1 : pas de fils gauche
             * Le sous-arbre droit prend la place du nœud.
             */
            if (A.gauche() == null) {
                return new Resultat(A.droite(), -1);
            }

            /*
             * CAS 2 : pas de fils droit
             * Le sous-arbre gauche prend la place du nœud.
             */
            if (A.droite() == null) {
                return new Resultat(A.gauche(), -1);
            }

            /*
             * CAS 3 : les deux fils existent
             * On utilise le minimum du sous-arbre droit.
             *
             * Dans un ABR :
             *     tous les éléments de G < A.elt
             *     A.elt < tous les éléments de D
             *
             * Le minimum de D est donc la valeur qui peut remplacer A.elt sans casser l'ordre ABR.
             */
            A.elt = MIN(A.droite());

            // Maintenant que la valeur a été copiée, il faut réellement supprimer cette valeur du sous-arbre droit.
            Resultat r = supprMin(A.droite());

            // Mise à jour de D.
            A.D = r.arbre;

            // La variation de hauteur vient de D.
            int h = r.deltaHauteur;

            // Si la hauteur n'a pas changé, rien de plus n'est nécessaire.
            if (h == 0) {
                return new Resultat(A, 0);
            }

            // Mise à jour de la balance.
            A.bal = A.bal + h;
        }

        // EQUILIBRAGE EN REMONTANT
        A = equilibrer(A);

        /*
         * Si la nouvelle balance vaut 0, la hauteur du sous-arbre a diminué de 1.
         * Sinon la hauteur est restée identique.
         */
        if (A.bal == 0) {
            return new Resultat(A, -1);
        }

        return new Resultat(A, 0);
    }

    // Méthode publique de suppression.
    public static AVL supprimer(AVL A, int x) {

        Resultat r = supprimerRec(A, x);
        return r.arbre;
    }

    /*
     * AFFICHAGE INFIXE
     *
     * Cette méthode est très utile pour vérifier que l'AVL reste un ABR.
     * Un parcours infixe d'un ABR donne les valeurs dans l'ordre croissant.
     */
    public static void afficherInfixe(AVL A) {

        if (A != null) {

            // Gauche
            afficherInfixe(A.gauche());

            /*
             * Racine
             * On affiche également la balance pour vérifier l'équilibrage.
             */
            System.out.print(
                    A.elt + "(" + A.bal + ") "
            );

            // Droite
            afficherInfixe(A.droite());
        }
    }
}