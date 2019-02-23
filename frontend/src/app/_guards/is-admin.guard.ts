import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {LoginUtils} from '../_services/loginutils/loginutils.service';
import {Logger} from '@wuja/logger';

@Injectable()
export class IsAdminGuard implements CanActivate {
	constructor (private loginutils: LoginUtils, private log: Logger, private router: Router) {}
	canActivate(next: ActivatedRouteSnapshot,
				state: RouterStateSnapshot): Observable<boolean>|Promise<boolean>|boolean {
		const rule = this.loginutils.isRoleSufficient(['ADMIN']);
		if (!rule) this.router.navigate(['/403']);
		return rule;
	}
}
