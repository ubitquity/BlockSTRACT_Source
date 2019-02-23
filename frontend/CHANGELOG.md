# CHANGELOG

# 1.6.0
- podniesiona wersja angulara ^6.1
- podniesiona wersja RX do ^6
- Dodano generator z funkcjami: konfigurowanie gita, konfigurowanie środowisk i języka, czyszczenie z plików demo 
- dodano word-break w głównym cssie
- dodano ReloadUserResolver
- dodano opakowanie proxy z lepszym logowaniem i możliwością przechwytywania requestów
- usunięto wywoływanie metody logout przy niepoprawnym logowaniu
- dodano dekorator Remeber do zapmiętywania stanu klasy
- dodano drop-menu jako główny komponent do nawigacji 
- przepisano całkowicie demo animals pod kątem schematics
- dodano aliasy dla ścieżek typescriptowych
- dodano skrypt do podnoszenia wersji z każdym buildem
- dodano guarda na sprawdzanie uprawnień na podstawie listy uprawnień z fallbackiem do pola role z UserDto


# 1.0.3
### 10.06.2018
- dodano dekorator @LogInit
- dodano @AutoUnsubscribe
- dodano confirm-modal w case'ach i _components
- dodano wybór języka przy logowaniu, dodano zlokalizowane pipe'y waluty i czasu
- dodano custom resolver

# 1.0.2
### 13.02.2018
- usunięto wszystkie paczki wymagające korzystanie z prywatnych repozytoriów
- zmiana URL'a do backendu https://office.itcraft.pl/jira/browse/WS-164
- dodano guardy IsLogged oraz IsAdmin, usunięto HasCredentials [WS-162](https://office.itcraft.pl/jira/browse/WS-162)
- usunięto sprawdzanie logowania i whiteliste z app.component na rzecz guarda
- zmiana adresu api `/web-api-admin` => `/webapi-admin`


# 1.0.1
### 21.02.2018
- komponent AppHeader dostał flagę pozwalającą ukrywać nawigację do strony głównej (\[back\]="false")

# 1.0.0
### 08.02.2018

- Dodano regułę tslint no-console
- Dodano moduł ngx-datatables
- Podniesiono do wersji 5.2
- Dodano moduł core.module do serwisów
- Dodano Interceptor
- Dodano WEB-INF do tomcata
- Dodano ErrorsInfoComponent oraz ErrorsService do obsługi błędów
- Wyczyszczono całą zbędną strukturę ze starych elementów
- Dodano skrypty do budowania wersji prod i stag ze sprawdzaniem poprawności projektu
- Dodano skrypty do swaggera
- Dodano HeaderComponent
- Dodano narzędzia logowania w głównej belce (LayoutComponent)
- Dodano stopke
- Dodano pełnego cruda na userów oraz animals
- Przywrócono z zewnętrznego modułu serwis HttpAuth (_services/httpauth.service)
- Dodano flagi do kompilatora (>>ng6) do ulepszonej obsługa expressions w widokach
- Zamieniono obsługę ErrorsInfoComponent na FormFieldComponent
- Dodano komponent do generowania podglądu pliku i dodano w profilu case do aktualizacji pliku
- Dodano case z infinite scroll
- Dodano pełne case'y na formularze z walidatorami
- Zbudowano komponent formularzowy inlineErrors jako uzupełnienie do serverErrors oraz formField
- Dodano recovery password
- Zbudowano case Google Maps
- podniesiono tslinta do 5.9.1 i dodano do excludowania modele podczas lintowania
