// tslint:disable

import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {HttpAuth} from '@services/httpauth/http-auth.service';
import {shareReplay} from 'rxjs/operators';

@Injectable({
	providedIn: 'root'
})
export class <%= classify(name) %>Service {
	constructor (private http: HttpAuth) {}
	public options = {
		sizes: [
			{label: 5, value: 5},
			{label: 10, value: 10},
			{label: 20, value: 20},
			{label: 50, value: 50}
		],
		all<%= classify(name) %>Types: [
			{value: 'BIRD', label: 'Bird'},
			{value: 'FISH', label: 'Fish'},
			{value: 'MAMMAL', label: 'Mammal'},
			{value: 'REPTILE', label: 'Reptile'},
			{value: 'AMPHIBIAN', label: 'Amphibian'},
			{value: 'INSECT', label: 'Insect'}
		],
		sortingValues: [
			{label: 'Name, ascending', value: 'name,asc'},
			{label: 'Name, descending', value: 'name,desc'},
			{label: 'Type, ascending', value: 'type,asc'},
			{label: 'Type, descending', value: 'type,desc'},
			{label: 'Description, ascending', value: 'description,asc'},
			{label: 'Description, descending', value: 'description,desc'},
		]
	};

	get<%= classify(name) %>(filters = {}): Observable<any> {
		if (filters['page']) filters['page']--;
		return this.http.get('/<%= dasherize(name) %>', filters).pipe(shareReplay());
	}
}
