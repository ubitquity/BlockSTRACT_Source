import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {Logger} from '@wuja/logger';
import {LoginUtils} from '../_services/loginutils/loginutils.service';
import {environment} from '../../environments/environment';
import {HttpAuth} from '../_services/httpauth/http-auth.service';

@Injectable()
export class IsLoggedGuard implements CanActivate {
	private whitelist = environment.routeWhiteList;
	constructor(private router: Router, private loginutils: LoginUtils, private http: HttpAuth) {}
	canActivate(next: ActivatedRouteSnapshot,
				state: RouterStateSnapshot): Observable<boolean>|Promise<boolean>|boolean {
		const condition = !!this.loginutils.isLogged;
		const url = state.url;
		const backToLogin = () => this.router.navigate( [ '/login' ] );
		if (this.isWhiteListed(url)) return true;
		else {
			if (!condition) backToLogin();
			else this.http.get( '/resend-cookie' ).subscribe( null, err => backToLogin() );
		}
		return condition;
	}

	private isWhiteListed (url) {
		return this.whitelist.find(item => !!url.match(new RegExp(item)));
	}
}
