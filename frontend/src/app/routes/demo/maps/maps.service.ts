import { CityDto } from './../../../_interfaces/models/cityDto';
import { Injectable } from '@angular/core';
import { Logger } from '@wuja/logger';
import { HttpAuth } from '../../../_services/httpauth/http-auth.service';
import { UtilsService } from '../../../_services/utils/utils.service';
import { ListDtoOfCountryDto } from '../../../_interfaces/models/listDtoOfCountryDto';

@Injectable()
export class MapsService {

	constructor(
		private http: HttpAuth,
		private log: Logger,
		private utils: UtilsService) { }

	getCountries(onlyWithCities: boolean = false) {
		const params = {
			onlyWithCities: onlyWithCities
		};
		return this.http.get<ListDtoOfCountryDto>('/countries', params);
	}

	saveCity(dto: CityDto) {
		return this.http.post<void>('/cities', dto);
	}

	deleteCity(dto: CityDto) {
		return this.http.delete<void>('/cities/' + dto.id);
	}
}
