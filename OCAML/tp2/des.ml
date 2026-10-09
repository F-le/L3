(*Lancer de dés*)
let lancer_de () = 1 + random.int 6

(*Jouer une fois*)
let jouer () = 
    let rec aux score = 
        let d = lancer_de () in
        let score = score + d in
        print_string "Dé: ";
        print_int d;
        print_string " - Score: ";
        print_int score;
        print_newline ();
        if d=5 || d = 6 then aux score
        else score
    in aux 0

(*Jouer plusieurs fois*)
let ()=
    Random.self_init();
    for i=1 to 5 do
        print_string "--- Tour ";
        print_int i;
        print_string " ---";
        print_newline();
        let score_final = jouer() in
        print_string "Score final: ";
        print_int score_final;
        print_newline ()
    done;;

(*test*)
(*
let () =
  Random.self_init ();
  for i = 1 to 10 do
    print_string "--- Partie ";
    print_int i;
    print_string " ---";
    print_newline ();
    let s = jouer () in
    print_string "Score final : ";
    print_int s;
    print_newline ()
  done*)