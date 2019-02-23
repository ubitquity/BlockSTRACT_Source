
import {timer as observableTimer, Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, Resolve} from '@angular/router';
import {HttpAuth} from '../_services/httpauth/http-auth.service';
import {Logger} from '@wuja/logger';

@Injectable()
export class ExampleResolver implements Resolve<any> {
	constructor(private http: HttpAuth,
				private log: Logger) {}

	async resolve(route: ActivatedRouteSnapshot) {
		const t = 300;
		await observableTimer(t).toPromise();

		this.log.log(`resolver example, delay: ${t}`);
		return true;
	}
}
