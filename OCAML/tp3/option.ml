(* Question 1 *)
let racine_option x =
  if x >= 0.0 then Some (sqrt x)
  else None

(* Question 2 *)
let div_option x y =
  if y <> 0.0 then Some (x /. y)
  else None

(* Question 3 *)
let bind x f =
  match x with
  | None -> None
  | Some a -> f a

(* Question 4 - Version directe *)
let compose_directe f g x =
  match f x with
  | None -> None
  | Some b -> g b

(* Question 4 - Version avec bind *)
let compose f g x =
  bind (f x) g

(* Question 5 *)
let div_racine_option x y =
  compose (fun n -> div_option n y) racine_option x

(* Question 6 *)
let rec filter_option liste =
  match liste with
  | [] -> []
  | None :: reste -> filter_option reste
  | Some x :: reste -> x :: filter_option reste

(* Question 7 *)
let rec map_filter f liste =
  match liste with
  | [] -> []
  | x :: reste ->
      match f x with
      | None -> map_filter f reste
      | Some y -> y :: map_filter f reste

(* Question 8 *)
let racine_liste liste =
  map_filter racine_option liste
