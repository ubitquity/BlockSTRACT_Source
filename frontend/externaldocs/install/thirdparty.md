# Instalowanie zewnętrznych bibliotek (third party libs)

W celu zainstalowania zewnętrzynch bibliotek należy zatrzymać projekt (uruchomione polecenia npm start, ng serve) oraz wpisać `npm install --save <nazwa_paczki>`

Dla przykładu zainstalujemy i wykorzystamy projekt ngx-datatables (https://github.com/swimlane/ngx-datatable)

### 1. Sprawdzenie poprawności projektu
Najważniejszym elementem poza sprawdzeniem czy paczka spełnia nasze oczekiwania jest sprawdzenie czy:
* czy biblioteka jest _dojrzała_ - jest długo rozwijana, pod wiele wersji, posiada mało zgłoszonych błędów, jest wstecz kompatybilna (obsługa angulara 5 oznacza zazawyczaj obsługę od 2 do latest, dobrze napisana obsługa 4+ również chociaż aot może sprawiać problemy, 2 lub 2+ zazwyczaj oznacza słabą wersję ktora nie jest rozwijana dalej), odpadają wersję mniejsze niż 1, i projekty młodsze niż pół roku.
* czy jest _aktywnie rozwijana_ - czy autorzy na bieżąco wrzucają patche przy każdej wersji minor/patch angulara, czy ostatni update nie był później niż pół roku temu (nowa wersja angulara jest co pół roku, jeśli ostatnia wersja biblioteki była pół roku temu najprawdopobniej będzie miała jakieś problemy), czy nie istnieje ryzyko że paczka zostanie porzucona
* Czy nie jest _overkillem_ - przykładowo biblioteka rx zawiera ponad sto dekoratorów które można zaimportować na raz ale można też wybiórczo zaimportować kilka potrzebnych. Jeśli rozwiązanie typu autocomplete wymaga zainstalowania całego zestawu ala ng-bootstrap to warto poszukać pojedynczego rozwiązania.


#### ... gdy nie znaleziono pakietu
Czasami daną bibliotekę jest trudno odnaleźć - czy to z powodu zbyt błachego czy zbyt skomplikowanego problemu, warto przemyśleć wtedy czy nie próbujemy wyważyć drzwi już otwartych. Angular jest rozwiązaniem kompleksowym i posiada ogrom wbudowanych helperów, inne helpery to lodash/moment - w miare rozwoju projektu będziemy dodawać wiele własnych. Podsumowując jeśli jakiejś biblioteki nie możemy znaleźć to warto przemyśleć napisanie własnej.


### 2. Instalacja
W większości bibliotek instalacja jest identyczna, w pierwszym kroku instalujemy paczkę z repozytoriów: 
```
npm i @swimlane/ngx-datatable --save
``` 

(literka i oznacza alias do install, --save oznacza że wpis zostanie dodany do pacakge.json)


#### Style
Jest to moduł z komponentami wizualnymi, najczęściej w takich komponentach wymagane jest jeszcze podpięcie styli ( patrz _2. Instalacja/style_ ). W tym przypadku style podepniemy poprzez dodanie do głównego pliku _style.scss_ importów relatywnych:

```
@import '../node_modules/@swimlane/ngx-datatable/release/index';
@import '../node_modules/@swimlane/ngx-datatable/release/themes/bootstrap';
```


#### Moduły
Wg dokumentacji biblioteki ostatnim etapem jest podłączenie modułów. Najczęstszym sposobem instalacji podanym w dokumentacjach jest najprostszy (niespotykany wręcz) przypadek konfiguracji projektu angularowego. Zakłada on że wszystko jest zainstalowanego do głównego modułu i nie posiada on struktury drzewiastej. Nasz projekt seedowy posiada zalecaną rozbudowaną konfigurację a co za tym idzie, należy ten fragment dokumentacji przetłumaczyć na nasz Seed. W tym wypadku będziemy instalować bibliotekę nie do głównego modułu `app.module` a do współdzielonego `shared.module` (w tej sytuacji zaimportowany moduł należy również dodać do eksportu). 


#### Użytkowanie
Ponieważ nasz instalacja jest już nieco niestandardowa - należy pamiętać że aby użyć biblioteki w dowolnym komponencie musimy w module trzymającym ten komponent również zaimportować moduł `shared.module`
 

#### Dokumentacja i przykłady
https://github.com/swimlane/ngx-datatable/tree/master/demo/basic



