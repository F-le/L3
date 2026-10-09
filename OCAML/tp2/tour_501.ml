let lancer_de () = 1 + random.int 6

(*tour_501 int-> int*)
let tour_501 score = 
  let total = lancer_de () + lancer_de () + lancer_de () + in
  if total <= score then score - total
  else score

(*jouer_501 int-> unit*)
let jouer_501 nb_joueurs = 
  let score = Array.make nb_joueurs 501 in
  let gagnant = ref (-1) in                 (* ref, car on doit le modifier ; -1 = personne n'a encore gagné *)
  while !gagnant = -1 do
    for i = 0 to nb_j - 1 do
      if !gagnant = -1 then begin           (* on arrête de faire jouer les autres dès qu'on a un gagnant *)
        tab_score.(i) <- tour_501 tab_score.(i);
        if tab_score.(i) = 0 then gagnant := i
      end
    done
  done;

print_string "Joueur gagnant: ";
print_int (!gagnant + 1);                   (* +1 pour afficher un numéro de joueur à partir de 1 *)
print_newline ();



let () =
  print_string "--- Exercice 1 ---";
  print_newline ();
  for i = 1 to 3 do
    let s = jouer () in
    print_string "Score final tour "; print_int i;
    print_string " : "; print_int s;
    print_newline ()
  done;

  print_string "--- Exercice 2 ---";
  print_newline ();
  jouer_501 4;;