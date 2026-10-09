let booleen msg =
  let () = Printf.printf "%s [On] " msg in
  let s = read_line () in
  match String.uppercase_ascii s with
  | "" | "Y" | "YES" | "O" | "OUI" -> true
  | "N" | "NO" | "NON" -> false
  | _ -> failwith "L'entree n'est pas un booleen"

let entier msg =
  let () = Printf.printf "%s " msg in
  let s = read_line () in
  match int_of_string s with
  | n -> n
  | exception Failure _ -> failwith "L'entree n'est pas un entier"
