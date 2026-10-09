let applique_plus_un x f =
  print_int x;
  let y = x + 1 in   (* il manquait le "in" : un "let" local doit toujours être suivi de "in" *)
  f y

let resultat = applique_plus_un 5 (fun x -> x)
(* "y" n'existe nulle part à cet endroit : c'est une variable locale à la fonction,
   invisible depuis l'extérieur. Il fallait donner une vraie valeur entière (ici 5). *)