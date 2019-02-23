import { CountryDto } from './../../../_interfaces/models/countryDto';
import { CityDto } from './../../../_interfaces/models/cityDto';
import { ListDtoOfCountryDto } from './../../../_interfaces/models/listDtoOfCountryDto';
import { MapsService } from './maps.service';
import { Component, OnInit } from '@angular/core';
import * as _ from 'lodash';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';

@Component({
	selector: 'app-maps',
	templateUrl: './maps.component.html',
	styleUrls: ['./maps.component.scss']
})
@AutoUnsubscribe()
@LogInit()
export class MapsComponent implements OnInit {

	ready = false;
	updating = false;
	nonEmptyCountries: CountryDto[] = [];
	allCountries: CountryDto[] = [];
	selectedCountry: CountryDto = null;
	selectedCountryId: number = null;

	public countries$;

	constructor(
		private mapsService: MapsService) {}

	ngOnInit() {
		this.loadCountries();
	}

	loadCountries() {
		this.countries$ = this.mapsService.getCountries()
			.subscribe(res => {
				this.accept(res);
			});
	}

	accept(res: ListDtoOfCountryDto) {
		this.ready = true;
		this.updating = false;
		this.allCountries = res.list;
		this.nonEmptyCountries = _.filter(this.allCountries, country => country.cities.length > 0);
		if (this.nonEmptyCountries.length > 0) {
			if (this.selectedCountryId != null) {
				const country: CountryDto = _.find(this.nonEmptyCountries, c => c.id === this.selectedCountryId);
				this.select(country);
			} else {
				this.select(this.nonEmptyCountries[0]);
			}
		} else {
			this.select(null);
		}
	}

	select(country?: CountryDto) {
		this.selectedCountry = country;
		if (country != null) {
			this.selectedCountryId = country.id;
		} else {
			this.selectedCountryId = null;
		}
	}

	countryChanged() {
		if (this.selectedCountry != null) {
			this.selectedCountryId = this.selectedCountry.id;
		} else {
			this.selectedCountryId = null;
		}
	}

	deleteCity($event: CityDto) {
		if (this.updating) {
			return;
		}
		this.updating = true;
		this.mapsService.deleteCity($event).subscribe(res => {
			this.loadCountries();
		}, err => {
			this.updating = false;
		});
	}

	saveCity($event: CityDto) {
		if (this.updating) {
			return;
		}
		this.updating = true;
		this.mapsService.saveCity($event).subscribe(res => {
			this.loadCountries();
		}, err => {
			this.updating = false;
		});
	}

}
