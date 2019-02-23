# Moduły

Webseed został zbudowany na bazie zalecanego guidelines Angular Team. Dokument ten zaleca konfigurowanie nowych projektów w następujący sposób:

## App.module
Jest to główny moduł - jest on bootstrapowany jako pierwszy i dostępny w każdej sytuacji. w najprostszych scenariuszach, w tym module, znajdują się importy do podstawowych bibliotek, providerów czy komponentów 

## Shared.module
Moduł ten skupia się na dodawaniu funkcjonalności potrzebnych w projekcie _on demand_. W miejscu tym głównie znajdują się komponenty wizualne lub dyrektywy. W module tym nie powinny się znajdować serwisy (oraz wszelkie podobne - np pipe'y)

## Core.module
Core to moduł ktory jest zaimportwany do `app.module` - jego główną rolą jest utworzenie pojedynczych instancji głównych serwisów. W angularze 2+ bardzo ważne jest utrzymywanie serwisów zarówno stanowych jak i bezstanowych tylko w jednej instancji. Najlepszym rozwiązaniem do tego problemu jest utworzenie modułu bezpośrednio zależnego od roota (app.module -> core.module) i w nim utworzenie tych serwisów. Core.module udostępnia swoje serwisy w góre drzewa dzięki czemu każde kolejne zaimportowanie w dowolnym miejscu projektu jednego z serwisów w tym module spowoduje wstrzyknięcie już stworzonego serwisu zamiast utworzenie nowej instancji.

Tip: jeśli potrzebujesz utworzyć globalny serwis stanowy dodaj go właśnie w tym module   
