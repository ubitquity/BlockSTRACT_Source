# Srodowiska (environments)

W angularze w wersji drugiej zostały dodane środowiska. w wersji 4+ można definiować dowolne środowiska. Podstawowe, dostępne zawsze to prod i dev. Pliki env zawierają zmienne pod konkretne buildy, czyli np plik environment.prod.ts zawiera ustawioną flagę `production: true` oraz adres api ustawiony na produkcyjne. W projekcie zawsze korzystamy tylko i wyłącznie z pliku environemnt.ts - jest on nadpisywany w zależności od tworzonego builda (np podczas budowania -prod plik environment.ts jest nadpisywany zawartością environemnt.prod.ts)

### Dodawanie środowiska

Aby zdefiniować nowe środowisko należy
* w katalogu ./src/environment utworzyć (najlepiej zduplikować obecny) plik `environment.{nowy_env}.ts`
* w pliku ./.angular-cli.json w `apps/environments` dodać definicje środowiska wskazującą na nasz powyższy plik


### Korzystanie z środowiska
Aby skorzystać z utworzonego środowiska należy wywołać komendy:
* tylko do zbudowania wersji `ng build --env={nowy_env}`
* do serwowania wersji `ng serve --env{nowy_env}` 
