# Angular Command Line interface - CLI

Najważniejszym narzędziem dostarczonym razem z instalacją angular jest @angular/cli, Biblioteka ta jest odpowiedzialna za, przede wszystkim, uruchamianie projektu i budowanie go (komendy ng serve oraz ng start)

Posiada również dwie inne komendy które są bardzo przydatne:

## `ng generate  <schematics> [name] <options...>`
Jest to zalecana metoda do tworzenia nowych elementów naszego projektu. Poza najważniejszą funkcją jaką jest utworzenie całej struktury, np komponentu, komenda ta wywołana prawidłowo spowoduje również zainstalowanie w nadrzędnym module danego komponentu (odpowiednio dodaje definicje w deklaracjach lub w importach). Warto zauważyć że na starcie dostępna jest spora gama schematów poza domyślnymi - można w ten sposób tworzyć interfejsy czy enumy.

## `ng lint`   
Głównym zadaniem tego polecenia jest przepuszczenie przez wbudowany w projekt linter naszego kodu. Podczas budowania webseeda bardzo zalecane jest używanie w swoim środowisku lintera ktory będzie sprawdzał poprawność na bieżąco. Przed buildami zaleca się sprawdzać tym narzędziem cały projekt. 
