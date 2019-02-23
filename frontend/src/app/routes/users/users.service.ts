import { UserDetailsDto } from './../../_interfaces/models/UserDetailsDto';
import { Injectable } from '@angular/core';
import { Logger } from '@wuja/logger';
import { HttpAuth } from '../../_services/httpauth/http-auth.service';

@Injectable()
export class UsersService {
	constructor(private http: HttpAuth, private log: Logger) { }

	public options = {
		sizes: [
			{label: 5, value: 5},
			{label: 10, value: 10},
			{label: 20, value: 20},
			{label: 50, value: 50}
		],
	};

	getUsers(filters = {}) {
		if (filters['page']) filters['page']--;
		return this.http.get('/abstractors', filters);
	}

	saveUser (data) {
		return this.http[data.id ? 'put' : 'post']('/abstractors', data);
	}

	getUser(id) {
		return this.http.get<UserDetailsDto>(`/abstractors/${id}`);
	}

	deleteUser(id) {
		return this.http.delete(`/abstractors/${id}`);
	}

	toggleAbstractor(data, id) {
		if (data.enabled) {
			return this.http.put(`/abstractors/${id}/activate`);
		} else {
			return this.http.put(`/abstractors/${id}/deactivate`);
		}
	}
}
