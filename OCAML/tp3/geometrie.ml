(* Question 1 *)
type point2D = float * float

(* Question 2 *)
type point3D = float * float * float

(* Question 3 *)
type point =
  | Point2D of point2D
  | Point3D of point3D

(* Question 4 *)
let projette p =
  match p with
  | Point2D (x, y) -> (x, y)
  | Point3D (x, y, z) -> (x, y)

(* Question 5 *)
let vers_3D p =
  match p with
  | Point2D (x, y) -> (x, y, 0.0)
  | Point3D (x, y, z) -> (x, y, z)

let distance p1 p2 =
  let (x1, y1, z1) = vers_3D p1 in
  let (x2, y2, z2) = vers_3D p2 in
  sqrt (
    (x1 -. x2) *. (x1 -. x2) +.
    (y1 -. y2) *. (y1 -. y2) +.
    (z1 -. z2) *. (z1 -. z2)
  )

(* Question 6 *)
type polygone = point2D list

(* Question 7 *)
let distance2D (x1, y1) (x2, y2) =
  sqrt (
    (x1 -. x2) *. (x1 -. x2) +.
    (y1 -. y2) *. (y1 -. y2)
  )

let rec max_distance p liste =
  match liste with
  | [] -> 0.0
  | q :: reste ->
      max (distance2D p q) (max_distance p reste)

let rec diametre polygone =
  match polygone with
  | [] -> 0.0
  | p :: reste ->
      max (max_distance p reste) (diametre reste)
