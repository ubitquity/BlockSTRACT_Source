import {Injectable} from '@angular/core';
import {HttpAuth} from '../../../_services/httpauth/http-auth.service';
// import {Httpclient} from '../../../_services/httpclient/httpclient.service';

@Injectable()
export class LoginService {

	constructor(
		private http: HttpAuth
	) {}

	login (email, password) {
		if (!email || !password) return;
		return this.http.post(`/login`, {email, password});
	}

	sendRecovery (email) {
		return this.http.post('/password/reset', {email});
	}

	sendNewPassword (dataToken) {
		return this.http.post('/password/set-new', dataToken);
	}
}
