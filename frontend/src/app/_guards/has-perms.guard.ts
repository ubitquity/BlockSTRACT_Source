import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router} from '@angular/router';
import {Observable} from 'rxjs';
import {LoginUtils} from '@services/loginutils/loginutils.service';
import {Logger} from '@wuja/logger';

@Injectable({
	providedIn: 'root'
})
export class HasPermsGuard implements CanActivate {
	constructor (private loginutils: LoginUtils, private router: Router, private log: Logger) {}
	canActivate(
		next: ActivatedRouteSnapshot,
		state: RouterStateSnapshot): Observable<boolean>|Promise<boolean>|boolean {
		const perms = next.data['perms'] || [];
		const condition = this.loginutils.isPermitted(perms);

		this.log.subscription('🔑 permission check', perms, condition);
		if (condition) return true;
		if (!condition) this.router.navigate(['/403']);
	}
}
