import java.util.*;

public class Main {

    // ---------- utilitaires ----------
    static void infixe(AB a, List<Integer> l) {
        if (a == null) return;
        infixe(a.gauche(), l);
        l.add(a.elt());
        infixe(a.droite(), l);
    }

    static List<Integer> liste(AB a) {
        List<Integer> l = new ArrayList<>();
        infixe(a, l);
        return l;
    }

    static void check(boolean cond, String msg) {
        if (!cond) throw new AssertionError("ECHEC : " + msg);
        System.out.println("  [ok] " + msg);
    }

    static AVL construire(int... vals) {
        AVL a = null;
        for (int v : vals) a = AVL.ajouter(a, v);
        return a;
    }

    static void titre(String s) {
        System.out.println("\n=== " + s + " ===");
    }

    // ---------- main ----------
    public static void main(String[] args) {
        testAB();
        testABR();
        testAVL();
        testExercices6et7();
        stressAVL();
        System.out.println("\nTous les tests sont passés.");
    }

    // ----- Exercice 1 + 4 -----
    static void testAB() {
        titre("AB (ex. 1 et 4)");
        //        1
        //       / \
        //      2   3
        //     / \   \
        //    4   5   6
        AB ab = AB.cons(1,
                AB.cons(2, AB.cons(4, null, null), AB.cons(5, null, null)),
                AB.cons(3, null, AB.cons(6, null, null)));

        System.out.print("Préfixe : ");
        AB.afficherPrefixe(ab);
        System.out.println();

        check(ab.rechercher(5) != null && ab.rechercher(5).elt() == 5, "rechercher(5) trouve le nœud 5");
        check(ab.rechercher(3).elt() == 3, "rechercher(3) retourne le sous-arbre de racine 3");
        check(AB.vide(ab.rechercher(42)), "rechercher(42) -> arbre vide");
        check(AB.hauteur(ab) == 3, "hauteur = 3");
        check(AB.taille(ab) == 6, "taille = 6");
        System.out.println("Parenthésage : " + AB.parenthese(ab));
        check(AB.parenthese(ab).equals("((4) 2 (5)) 1 (3 (6))"), "parenthésage correct");
        check(AB.hauteur(null) == 0 && AB.taille(null) == 0, "arbre vide : h = 0, taille = 0");
    }

    // ----- Exercice 2 + 5 -----
    static void testABR() {
        titre("ABR (ex. 2 et 5)");
        ABR t = ABR.arbre_vide();
        for (int v : new int[] { 8, 3, 10, 1, 6, 14, 4, 7, 13 }) t = ABR.ajouter(t, v);

        System.out.println("Infixe : " + liste(t));
        check(liste(t).equals(Arrays.asList(1, 3, 4, 6, 7, 8, 10, 13, 14)), "infixe trié");
        check(AB.estABR(t), "estABR");
        check(t.MAX() == 14, "MAX = 14");
        check(ABR.rechercher(t, 6) != null && ABR.rechercher(t, 6).elt() == 6, "rechercher(6)");
        check(ABR.rechercher(t, 5) == null, "rechercher(5) absent");
        check(liste(ABR.ajouter(t, 8)).size() == 9, "pas de doublon à l'ajout");

        ABR s = ABR.supprimer(t, 3);   // deux fils
        check(liste(s).equals(Arrays.asList(1, 4, 6, 7, 8, 10, 13, 14)) && AB.estABR(s), "supprimer(3) : deux fils");
        s = ABR.supprimer(s, 8);       // la racine
        check(liste(s).equals(Arrays.asList(1, 4, 6, 7, 10, 13, 14)) && AB.estABR(s), "supprimer(8) : racine");
        s = ABR.supprimer(s, 99);      // absent
        check(liste(s).size() == 7, "supprimer(99) absent : rien ne change");

        // Exercice 5
        for (int x : new int[] { 7, 5, 0, 20 }) {
            ABR[] p = ABR.diviser(t, x);
            boolean ok = AB.estABR(p[0]) && AB.estABR(p[1]);
            for (int v : liste(p[0])) ok &= v <= x;
            for (int v : liste(p[1])) ok &= v > x;
            ok &= AB.taille(p[0]) + AB.taille(p[1]) == AB.taille(t);
            System.out.println("  diviser(" + x + ") : <= : " + liste(p[0]) + "   > : " + liste(p[1]));
            check(ok, "diviser(t, " + x + ") correct");
        }
        check(liste(t).size() == 9, "l'arbre d'origine n'est pas modifié par diviser");
    }

    // ----- Exercice 3 -----
    static void testAVL() {
        titre("AVL (ex. 3)");

        // Les 4 rotations : la racine doit devenir 2 dans chaque cas
        check(construire(3, 2, 1).elt() == 2, "ROTD         (3,2,1)");
        check(construire(1, 2, 3).elt() == 2, "ROTG         (1,2,3)");
        check(construire(3, 1, 2).elt() == 2, "ROTGD (double) (3,1,2)");
        check(construire(1, 3, 2).elt() == 2, "DROTG (double) (1,3,2)");

        AVL a = construire(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15);
        System.out.print("Infixe avec balances : ");
        AVL.afficherInfixe(a);
        System.out.println();
        check(AB.hauteur(a) == 4, "15 insertions croissantes -> arbre parfait de hauteur 4");
        check(a.elt() == 8, "racine = 8");
        check(AB.estAVL(a) && AVL.balancesCoherentes(a), "estAVL + balances cohérentes");
        check(AVL.rechercher(a, 11) != null && AVL.rechercher(a, 11).elt() == 11, "rechercher(11)");
        check(AVL.rechercher(a, 42) == null, "rechercher(42) absent");
        check(a.rechercher(4).elt() == 4, "version d'instance rechercher(4)");

        a = AVL.ajouter(a, 8);
        check(AB.taille(a) == 15, "ajout d'un doublon sans effet");

        for (int x : new int[] { 1, 2, 3, 4, 8 }) {
            a = AVL.supprimer(a, x);
            check(AB.estAVL(a) && AVL.balancesCoherentes(a), "après supprimer(" + x + ") : toujours un AVL");
        }
        System.out.print("Infixe après suppressions : ");
        AVL.afficherInfixe(a);
        System.out.println();
        check(liste(a).equals(Arrays.asList(5, 6, 7, 9, 10, 11, 12, 13, 14, 15)), "contenu correct");

        AVL vide = AVL.supprimer(null, 3);
        check(AVL.vide(vide), "supprimer dans arbre vide");
    }

    // ----- Exercices 6 et 7 -----
    static void testExercices6et7() {
        titre("Exercices 6 et 7");

        // Dégénéré : ABR non équilibré
        ABR deg = null;
        for (int v = 1; v <= 5; v++) deg = ABR.ajouter(deg, v);
        Map<AB, Integer> bal = AB.calculerBalances(deg);
        System.out.println("  Balances de l'ABR dégénéré (racine) : " + bal.get(deg));
        check(bal.get(deg) == 4, "balance de la racine de 1->2->3->4->5 = 4");
        check(AB.estABR(deg) && !AB.estAVL(deg), "ABR dégénéré : estABR oui, estAVL non");

        // AB équilibré mais pas ABR
        AB pasABR = AB.cons(5, AB.cons(9, null, null), AB.cons(1, null, null));
        check(!AB.estABR(pasABR) && !AB.estAVL(pasABR), "AB équilibré mais pas ABR : pas un AVL");

        // Un ABR qui viole la règle plus bas (3 est à gauche de 5 mais dans le sous-arbre droit de 4)
        AB piege = AB.cons(4, AB.cons(2, null, null), AB.cons(6, AB.cons(3, null, null), null));
        check(!AB.estABR(piege), "piège : 3 dans le sous-arbre droit de 4 -> pas un ABR");

        // Un vrai AVL
        AVL v = construire(5, 3, 8, 1, 4, 7, 9);
        check(AB.estAVL(v), "AVL construit par ajouter : estAVL");

        // Recalcul des balances dans un AVL dont les champs sont faux
        AVL x = new AVL(2, new AVL(1), new AVL(3));
        AVL.recalculerBalances(x);
        check(AVL.balancesCoherentes(x), "recalculerBalances");
    }

    // ----- Test de stress AVL vs TreeSet -----
    static void stressAVL() {
        titre("Test de stress AVL (aléatoire)");
        Random rnd = new Random(42);
        TreeSet<Integer> ref = new TreeSet<>();
        AVL a = null;

        for (int i = 0; i < 20000; i++) {
            int x = rnd.nextInt(500);
            if (rnd.nextBoolean()) {
                a = AVL.ajouter(a, x);
                ref.add(x);
            } else {
                a = AVL.supprimer(a, x);
                ref.remove(x);
            }
            if (!AB.estAVL(a))
                throw new AssertionError("Plus un AVL après l'opération n°" + i);
            if (!AVL.balancesCoherentes(a))
                throw new AssertionError("Balances stockées incohérentes à l'opération n°" + i);
            if (!liste(a).equals(new ArrayList<>(ref)))
                throw new AssertionError("Contenu différent de TreeSet à l'opération n°" + i);
            int n = ref.size();
            if (AB.hauteur(a) > 1.45 * Math.log(n + 2) / Math.log(2))
                throw new AssertionError("Hauteur trop grande à l'opération n°" + i);
        }
        check(true, "20000 opérations aléatoires : AVL valide, balances OK, contenu = TreeSet, h = O(log n)");
    }
}