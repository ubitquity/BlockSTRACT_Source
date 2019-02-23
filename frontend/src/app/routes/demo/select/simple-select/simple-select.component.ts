import { Component, OnInit } from '@angular/core';

@Component( {
	selector: 'app-simple-select',
	template: `
		<ng-select [items]="cities"
				   bindLabel="name"
				   placeholder="Select city"
				   [(ngModel)]="selectedCity">
		</ng-select>

		<p class="mt-1 mb-3 text-muted">
			Selected city model: {{selectedCity | json }}
		</p>
		
		<ng-select [items]="cities"
				   bindLabel="name"
				   bindValue="id"
				   placeholder="Select city"
				   [(ngModel)]="selectedCityId">
		</ng-select>
		
		<p class="mt-1 mb-3 text-muted">
			Selected city id: {{selectedCityId | json }}
		</p>

		<ng-select [items]="cities"
				   bindLabel="name"
				   bindValue="id"
				   [searchable]="false"
				   placeholder="Select city (dropdown like)"
				   [(ngModel)]="selectedCityId">
		</ng-select>
	`
} )
export class SimpleSelectComponent implements OnInit {

	cities = [
		{id: 1, name: 'Vilnius'},
		{id: 2, name: 'Kaunas'},
		{id: 3, name: 'Pabradė'}
	];
	selectedCity: any;
	selectedCityId: any;

	constructor() {}

	ngOnInit() {
	}

}
