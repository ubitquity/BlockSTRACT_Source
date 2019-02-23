import { Injectable } from '@angular/core';
import { HttpAuth } from '../../_services/httpauth/http-auth.service';
import { LoginUtils } from '../../_services/loginutils/loginutils.service';
import * as _ from 'lodash';
import { UserDetailsDto } from '../../_interfaces/models/UserDetailsDto';

@Injectable()
export class ProfileService {

	constructor(private http: HttpAuth, private loginutils: LoginUtils) {}

	get profile () {
		return this.loginutils.user;
	}

	getProfile () {
		return this.http.get('/admin');
	}


	updateProfile(data) {
		return this.http.put('/admin', data);
	}

	importFromQualia() {
		return this.http.post('/qualia/import');
	}

	getAbstractorProfile() {
		return this.http.get('/abstractor-profile');
	}

	getCounties() {
		return this.http.get('/counties/');
	}
	getServices() {
		return this.http.get('/services/');
	}

	saveAbstractorProfile(data) {
		return this.http.put('/abstractor-profile', data);
	}
}
