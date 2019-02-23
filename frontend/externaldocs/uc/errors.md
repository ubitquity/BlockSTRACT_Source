# Obsługa błędów

Błędy w webseedzie obsługujemy dwoma narzędziami: 

### 1. [ErrorsInfoComponent](/components/ErrorsInfoComponent.html) 
Jest to komponent odpowiedzialny za wyświetlanie błędów pochodzących z backendu, zakładając że dostajemy błędy w takim DTOsie:

```
// dodać dtosa
```

oraz zakładając że błędy obsługujemy w taki sposób:

``` typescript
this.service.sendRequest(newrequest))
	.subscribe(
		res => {
			// … success
		},
		err => {
			// errors
			this.sendErrors = err;
		});
```

użycie komponentu przedstawia się w taki sposób:
```html
<app-errors-info field="shortDescription" [errors]="sendErrors?.error.fieldErrors"></app-errors-info>
```

gdzie wartość `field` to zwykły string który jest kluczem w `sendErrors.error.fieldErrors`


### 2. ErrorsService
To serwis do domyślnej obsługi błędów - jego obsługa jest podzielona na dwie części: podstawowa obsługa wszystkich błędów oraz przekierowanie błędu na konkretny kod. Do tej pory w naszych projektach mieliśmy defaultową obsługę kodu 401 która w przypadku wystąpienia powodowała wylogowanie użytkownika. 
											  

