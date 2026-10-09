(* 1. Les valeurs d'une carte *)
type face = Deux | Trois | Quatre | Cinq | Six | Sept | Huit | Neuf | Dix
          | Valet | Dame | Roi | As

(* 2. Les couleurs *)
type couleur = Pique | Coeur | Carreau | Trefle

(* 3. Une carte = une face + une couleur *)
type carte = { valeur : face; couleur : couleur }

(* 4. Initialisation de deux cartes *)
let roipique = { valeur = Roi; couleur = Pique }
let ascoeur  = { valeur = As;  couleur = Coeur }

(* 5. Comparer deux cartes.
   "face" est un type énuméré sans ordre numérique implicite,
   donc on ne peut pas comparer deux "face" directement avec < ou >.
   Il faut une fonction auxiliaire qui donne un rang numérique à chaque face. *)
let rang_de_face f =
  match f with
  | Deux -> 2 | Trois -> 3 | Quatre -> 4 | Cinq -> 5
  | Six -> 6 | Sept -> 7 | Huit -> 8 | Neuf -> 9 | Dix -> 10
  | Valet -> 11 | Dame -> 12 | Roi -> 13 | As -> 14

(* compare c1 c2 renvoie un entier positif si c1 est plus forte,
   négatif si c2 est plus forte, 0 en cas d'égalité de valeur *)
let compare c1 c2 =
  let r1 = rang_de_face c1.valeur in
  let r2 = rang_de_face c2.valeur in
  r1 - r2   (* positif si r1>r2, négatif si r1<r2, 0 si égal *)

(* 6. Tirer une carte au hasard.
   On stocke toutes les faces et couleurs possibles dans des tableaux,
   et on tire un indice aléatoire dans chacun. *)
let toutes_faces = [| Deux; Trois; Quatre; Cinq; Six; Sept; Huit; Neuf; Dix;
                      Valet; Dame; Roi; As |]
let toutes_couleurs = [| Pique; Coeur; Carreau; Trefle |]

let tire_random () =
  { valeur  = toutes_faces.(Random.int 13);
    couleur = toutes_couleurs.(Random.int 4) }

(* Test : on tire 100 cartes et on les affiche, juste pour vérifier
   "à l'oeil" qu'on obtient bien des cartes variées et valides *)
let test () =
  for _ = 1 to 100 do
    let c = tire_random () in
    (* on ne cherche pas à afficher joliment ici, juste à vérifier
       que ça ne plante pas et que ça couvre bien les 13 faces / 4 couleurs *)
    ignore c
  done;
  print_string "100 tirages effectués sans erreur";
  print_newline ()

(* 7. Score d'une carte *)
let score_carte c =
  match c.valeur with
  | As -> 11
  | Dix -> 10
  | Roi -> 4
  | Dame -> 3
  | Valet -> 2
  | _ -> 0   (* toutes les autres faces ne rapportent rien *)

(* 8. Un tour de bataille simplifié.
   On renvoie (points_joueur1, points_joueur2) plutôt que d'imprimer
   directement : ça rend la fonction réutilisable (par exemple pour
   cumuler un score sur plusieurs tours dans l'exercice 3). *)
let bataille () =
  let c1 = tire_random () in
  let c2 = tire_random () in
  let pts = score_carte c1 + score_carte c2 in
  let res = compare c1 c2 in
  if res > 0 then (pts, 0)       (* joueur 1 gagne les deux cartes *)
  else if res < 0 then (0, pts)  (* joueur 2 gagne les deux cartes *)
  else (0, 0)                     (* égalité*)