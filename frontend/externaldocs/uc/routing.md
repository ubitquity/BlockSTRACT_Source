# Routing

więcej na https://angular.io/guide/router

W webseedzie routing został oparty o plik routes.ts. Plik ten jest wykorzystywany przez RoutesModule który jest zaimportowany bezpośrednio w główny moduł AppModule


### Ustawianie ścieżki do komponentu
W angularze 2+ wg zaleceń developerów struktura modułów odpowiada zawartości katalogu routes (./src/app/routes). Dany url może reprezentować zarówno komponent wkompilowany w główny bundle lub moduł leniwie ładowany (tak wiem jak to brzmi - ale wiadomo że chodzi lazyload). Moduł/komponent wkompilowany jest dostępny od razu po bootstrapowaniu aplikacji i nie jest potrzebne żadne doładowywanie. Scieżki takie ustaiwamy bezpośrednio na komponent w taki sposób

** ./src/app/routes.ts **
```javascript
{ path: 'login', component: LoginComponent },
``` 

Takich bezpośrednich wskazań na komponent powinno znaleźć się jak najmniej (no chyba że inaczej mówi polityka projektu) - powinny to być komponenty niezbędne do funkcjonowania jak logowanie, rejestracja czy strony błędów (im więcej takich modułów/komonentów wbudowanych w aplikacje tym dłuższy czas ładowania - initial loading). Drugim rodzajem ładowania komponentów jest przez lazy-loading

### Ustawianie ścieżki do modułu z własnym routingiem (lazy loading)
Drugim rodzajem przekierowania urla jest wskazanie na adres modułu. Moduł taki jest doładowywany _on demand_ - czyli dopiero po wywołaniu adresu zaciągany jest dany moduł.

moduł taki definiujemy w taki sposób:

** ./src/app/routes.ts **
```javascript
{ path: 'demo', loadChildren: './../../routes/demo/demo.module#DemoModule' },
```
(jest to string wskazujący na adres modułu oraz po # jego właściwa nazwa)

w pliku samego modułu należy umieścić routing relatywny dla danego modułu:

```typescript
const routes: Routes = [
	{
		path: '', component: HomeComponent, children: [
		{ path: 'buttons', component: ButtonsComponent },
		{ path: 'alerts', component: AlertsComponent },
		{ path: 'datepicker', component: DatepickerComponent }
	]
	}
];

@NgModule( {
	imports: [
		CommonModule,
		RouterModule.forChild( routes ),
		NgbModule,
		FormsModule
	],
	exports: [ RouterModule ],
	declarations: [ HomeComponent, ButtonsComponent, AlertsComponent, DatepickerComponent ]
} )
export class DemoModule {}
```

