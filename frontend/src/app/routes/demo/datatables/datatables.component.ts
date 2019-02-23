import { Component, OnInit } from '@angular/core';

@Component( {
	selector: 'app-datatables',
	templateUrl: './datatables.component.html',
	styleUrls: [ './datatables.component.scss' ]
} )
export class DatatablesComponent implements OnInit {
	rows = [
		{ name: 'Austin', gender: 'Male', company: 'Swimlane' },
		{ name: 'Dany', gender: 'Male', company: 'KFC' },
		{ name: 'Molly', gender: 'Female', company: 'Burger King' },
	];
	columns = [
		{ prop: 'name' },
		{ name: 'Gender' },
		{ name: 'Company' }
	];

	constructor() {
	}

	ngOnInit() {
	}

}
