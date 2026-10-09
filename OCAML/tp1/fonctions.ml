(* 1. Maximum de trois entiers *)
let max3 x1 x2 x3 =
  if x1 >= x2 && x1 >= x3 then x1
  else if x2 >= x3 then x2
  else x3;;

max3 4 9 10;;   (* int = 10 *)

(* 2. f(2x)**2 — deux écritures équivalentes *)
let calcul f x =
  let y = f (2 * x) in
  y * y;;

let calcul2 f x =
  f ((2 * x) * (2 * x));;

calcul (fun x -> x) 3;;    (* int = 36   (f(6)=6, 6*6=36) *)
calcul2 (fun x -> x) 3;;   (* int = 36 *)

(* 3. flip inverse l'ordre des arguments *)
let flip f x y = f y x;;
let add a b = a + b;;
flip add 3 5;;   (* équivaut à add 5 3 = 8 *)