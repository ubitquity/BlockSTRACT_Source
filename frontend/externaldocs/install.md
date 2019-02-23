# Instalacja

Aby zainstalować i zacząć używać webseeda, należy wykonać następujące kroki:
( repo: http://lab.itcraft.pl:8182/itcraft/web-front-core ) 


### 1. Sklonowanie projektu
```$ 
git clone git@lab.itcraft.pl:itcraft/web-front-core.git <nazwa_projektu>
```


### 2. Ustawianie informacji o projekcie
Przechodzimy do projektu a następnie czyścimy informacje o projekcie seedowym, na koniec inicjujemy nowy projekt gitowy
```
cd <nazwa_projektu>
rm -rf .git
git init
```

ostatecznie w pliku ./package.json ustawiamy wszystkie potrzebne informacje. ( numer wersji warto pozostawić taki jaki był jako referencja do debugowania względem seeda ) 


### 3. Instalacja paczek
Wywołujemy `npm install` w głównym katalogu ( ./ ) w celu instalacji wszystkich zależności


### 4. Uruchamianie 
Projekt uruchamiamy komendą `npm start` - po uruchomieniu projekt dostępny jest pod adresem `localhost:4200`


### 5. Ustawianie proxy do api
Z założenia w webie niemożliwe jest korzystanie z endpointów pod innym adresem niż obecny (np fajnyadres:90210 oraz fajnyadres:8080 to dwa inne adresy ). Jest to zabezpieczenie CORS (Cross Origin Resource Sharing). W tym celu możemy albo ustawić nagłówki (nie działa do lokalnego użytku) albo ustawić proxy na serwerze serwującym angulara.

* W pliku ./proxy.config.json należy zmienić `/md-patient-api/**` na `/dowolny-adres-z-api/**`, oraz `"target": "http://lab2.itcraft.pl:8080",` zamienić na porządany url (np lokalnie będzie to `http://localhost:8080` ). 

* aby wyłączyć proxy należy w pliku ./package.json wyłączyć proxy ze skryptu startowego czyli w komendzie `npm start` usunąć `"target": "http://lab2.itcraft.pl:8080",` ( lub korzystać z komendy `npm run start:noproxy` )


### 6. Zamrażanie wersji
Wersjami pakietów i zarządzaniem nimi zajmuje się node.js a dokładniej npm. Npm posługuje się głównym plikiem konfoguracyjnym package.json w którym zapisane są dyrektywy odnośnie tego jakie wersje w projekcie mogą zostać użyte. Istnieje cała masa składni na to jak wymusić użycie konkretnych wersji i jak takie warunki zapisać, najpopularniejszym jednak jest zapisywanie tego w takie spoób `^1.0.0`. Taki zapis mówi nam o tym że zostanie użyta wersja najniżej 1.0.0 a najwyżej 1.9x.9x (wersja major nigdy nie zmieni się na wyższą). Jeśli biblioteka stosuje sposób zapisu SEMVER to takie instalowanie paczek spowoduje że projekt będzie zawsze działał i trzeba doglądać projektu tylko przy zmianach typu minor. Na tym etapie warto wiedzieć że ten proces w naszym projekcie zachodzi bardzo rzadko, a przede wszystkim raz - na początku. Podczas pierwszej instalacji ( a dokładniej podczas pierwszego wywołania `npm install` ) utworzony zostaje plik package-lock.json (@node.js#8.x.x+). Plik ten trafia do repozytorium i jest następnym wyznacznikiem w instalowaniu paczek na każdym kolejnym urządzeniu. Plik package-lock.json zawiera informacje o tym w jakich technologiach został projekt wystartowany i w jakich napewno działa (pomijając fakt że od developera zależy czy sprawdził czy działa) - środowisko to będzie odtwarzane z każdą kolejną instalacją (bardzo ważne jest aby ten plik egzystował w repo).

W każdje chwili można natomiast łatwo zaktualizować taki projekt - wystarczy wywołać skrypt `npm run reinstall:hard` lub odpowiednio wykonać:
* usunąć plik ./.package-lock.json
* usunąć ./node_modules/
* wywołać jeszcze raz `npm install`


### 7. Blokowanie konkretnej wersji
Czasami może wystąpić wymóg korzystania z jakiejs konkretnej wersji bibliotek, wówczas będziemy zmuszeni zamrozić w dokładnych wersjach zależności nasz projekt. Aby ten cel osiągnąć musimy po zainstalowaniu wszystkich pakietów podejrzeć jakie zainstalowane wersje posiadamy i przepisać je do pliku package.json. Przykładowo w projekcie korzystamy z paczki `@itcraft/storage` chcemy używać konkretnie wersji 1.0.7, natomiast w package.json mamy wpisane ^1.0.0 co oznacza że zostanie zainstalowana wersja latest z brancha 1.x.x. W package.json należy zamienić powyższy wpis na dokładną wersję czyli 1.0.7 (bez strzałki na początku). Chociaż tutaj przykład jest błachy i moglibyśmy się ograniczyć do tylko tej paczki to w praktyce sugerowane jest przepisanie wszystkich wersji zainstalowanych pakietów (z pliku package-lock do package.json przepisujemy dokładnie takie wersje jakie się zainstalowały tj: @angular/core: "^5.0.0" -> "5.2.1")

Taką wersję projektu aktualizujemy ręcznie jeśli zajdzie potrzeba, tj ręcznie zmieniamy wersje paczek na wyższe/niższe    
