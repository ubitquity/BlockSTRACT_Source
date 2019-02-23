import { CityDto } from './../../../../_interfaces/models/cityDto';
import { CountryDto } from './../../../../_interfaces/models/countryDto';
import { Component, OnInit, EventEmitter, Output, Input } from '@angular/core';

@Component({
	selector: 'app-city-form',
	templateUrl: './city-form.component.html',
	styleUrls: ['./city-form.component.scss']
})
export class CityFormComponent implements OnInit {

	@Input()
	set allCountries(allCountries: CountryDto[]) {
		this.countries = allCountries;
		this.newCity = {
			id: null,
			countryId: null,
			latitude: 0,
			longitude: 0,
			name: null
		};
	}

	@Input()
	disabled = false;

	@Output()
	onSave = new EventEmitter<CityDto>();

	@Output()
	onDelete = new EventEmitter<CityDto>();

	countries: CountryDto[] = [];
	newCity: CityDto = {
		id: null,
		countryId: null,
		latitude: 0,
		longitude: 0,
		name: null
	};

	constructor() { }

	ngOnInit() {
	}
}
