import { Injectable } from '@angular/core';
import { HttpAuth } from '../../../_services/httpauth/http-auth.service';

@Injectable()
export class ActivateService {
	constructor(private http: HttpAuth) {}

	activateAbstractor(token) {
		return this.http.get(`/abstractor/activate/${token}`);
	}
}
