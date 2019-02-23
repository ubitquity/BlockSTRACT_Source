# Style

Projekty napisane w angularze 2+ korzystają z zasady styli komponentowych. Tzn że style są nadawane lokalnie per komponent a nie jak to miało miejsce dotychczas tylko i wyłącznie globalnie. Istnieje kilka sposobów definiowania styli:

### Definiowanie w pliku przypisanym do komponentu (domyślne) 
w definicji komponentu jest wskazanie na plik który w procesie kompilacji zostanie wciągnięty do głównego bundle'a, odpowiednio klasy css zostaną opatrzone atrybutami tworzącymi scope komponentu (dzięki temu rozwiązaniu dany styl jest dostępny tylko i wyłącznie dla danego komponentu i nigdzie poza nim) 

```
@Component( {
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: [ './home.component.scss' ]
} )
```

warto zauważyć że pole styleUrls jest tablicą więc plików można zdefiniować wiele ( kolejność plików ma zatem znaczenie )

### Definiowanie inline w meta komponentu
Powyższe stylowanie importuje cały plik i przetwarza go na jscss co oszczędza requestów, proces ten można jeszcze bardziej skrócić w przypadku mikro definicji dodając definicję inline:

```
@Component( {
	selector: 'app-home',
	templateUrl: './home.component.html',
	styles: ['h1 { font-weight: normal; }']
} )
```

### Definiowanie globalne
Jest to klasyczne definiowanie stylu. Tworzymy pliki w podstawowej strukturze w katalogu `./src/app/_styles`. Główny plik importujący wszystkie zależności to `./app/styles.scss` - w nim znajdują się wszystkie importy. W tym również główny plik bootstrapa.


## Stylowanie Bootstrapa
Bootstrap w wersji 4 został lekko zmieniony w stosunku do wersji trzeciej. W wersji trzeciej użytkownik zmuszony był napdisywać bezpośrednio zmienne bs po zaimportowaniu bootstrapa. w wersji czwartej użytkownik musi utworzyć przed zaimportowaniem bootstrapa te same zmienne z których korzysta bs - zmienne te zostaną użyte przez bs w trakcie kompilacji. 
