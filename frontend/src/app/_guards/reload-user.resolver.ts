import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {HttpAuth} from '../_services/httpauth/http-auth.service';
import {Logger} from '@wuja/logger';
import {LoginUtils} from '../_services/loginutils/loginutils.service';
import {UserDto} from '../_interfaces/models/UserDto';

@Injectable()
export class ReloadUserResolver implements Resolve<any> {
	constructor(private http: HttpAuth,
				private log: Logger,
				private loginutils: LoginUtils) {}

	resolve(route: ActivatedRouteSnapshot) {
		if (this.loginutils.isLogged) this.reloadUser();
		else this.loginutils.user = null;
		return true;
	}

	async reloadUser () {
		await this.http.get<UserDto>('/user/profile')
			.subscribe(res => {
				this.loginutils.user = res;
			}, err => {
				// this.log.error('no cookie attached, no hax pls', err);
				this.loginutils.user = null;
			});
		return true;
	}
}
