let rec pretty_ackermann m n =
  print_string "Ackermann ";
  print_int m;
  print_string " ";
  print_int n;
  print_newline ();
  if m = 0 then n + 1
  else if n = 0 then pretty_ackermann (m - 1) 1
  else pretty_ackermann (m - 1) (pretty_ackermann m (n - 1))

pretty_ackermann 2 1;;