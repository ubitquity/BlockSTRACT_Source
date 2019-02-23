# Przygotowanie wersji produkcyjnej

Zbudowanie wersji produkcyjnej ostatecznie ogranicza się do wywołania `ng build -prod`. Wersja ta jest dużo lżejsza od wersji dev i nadaje się do serwowania w dowolnym miejscu. Należy jednak pamiętać o kilku podstawowych aspektach zanim wypuścimy projekt na produkcje:

* w całym projekcie pozbywamy się warningów i błędów
* pozbywamy się użyć metod logowania - w tym celu wystarczy używać klasy Logger która sama wykrywa czy jesteśmy na produkcji czy nie. Kategorycznie nie używamy `console.*` - użycia console są zabronione i możemy je wykryć narzędziemy tslint które wskaże wystąpienia.
* wysoce wskazana jest optymalizacja requestów - w przypadku obrazków małe obrazki (svg i png) zamieniamy (png) na base64 i obsadzamy w kodzie scss. w przypadku większych obrazków optymalizujemy wielkość 
* … w przypadku skryptów - staramy się je dodać do procesu kompilacji - tzn jeśli skrypt nie jest instalowany via npm a jest konieczny do dodania np jako `<script>` wówczas dodajemy go do sekcji scripts w pliku .angular-cli.json    


