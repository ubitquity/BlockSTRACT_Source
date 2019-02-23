import {Injectable} from '@angular/core';
import {HttpAuth} from '../../../_services/httpauth/http-auth.service';
// import {Httpclient} from '../../../_services/httpclient/httpclient.service';

@Injectable()
export class RegisterService {

	constructor(
		private http: HttpAuth
	) {}

	register(data) {
		return this.http.post('/abstractor/signup', data);
	}
}
