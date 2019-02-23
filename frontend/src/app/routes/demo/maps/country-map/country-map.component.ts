import { CityDto } from './../../../../_interfaces/models/cityDto';
import { Component, OnInit, Input } from '@angular/core';
import { CountryDto } from '../../../../_interfaces/models/countryDto';

@Component({
	selector: 'app-country-map',
	templateUrl: './country-map.component.html',
	styleUrls: ['./country-map.component.scss']
})
export class CountryMapComponent implements OnInit {

	cities: CityDto[] = null;
	centerLatitude: number = null;
	centerLongitude: number = null;
	zoom: number = null;

	@Input()
	set country(country: CountryDto) {
		this.centerLatitude = 0;
		this.centerLongitude = 0;
		this.zoom = 2;
		if (country != null) {
			this.cities = country.cities;
			if (this.cities.length > 0) {
				for (const city of this.cities) {
					this.centerLatitude += city.latitude;
					this.centerLongitude += city.longitude;
				}
				this.centerLatitude /= this.cities.length;
				this.centerLongitude /= this.cities.length;
				this.zoom = 6;
			}
		} else {
			this.cities = [];
		}
	}

	constructor() { }

	ngOnInit() {
	}

}
