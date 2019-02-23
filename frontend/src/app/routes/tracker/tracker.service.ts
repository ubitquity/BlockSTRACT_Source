import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageOfAnimalDto} from '../../_interfaces/models/PageOfAnimalDto';
import {HttpAuth} from '../../_services/httpauth/http-auth.service';
import {shareReplay} from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { LoginUtils } from '../../_services/loginutils/loginutils.service';

@Injectable({
	providedIn: 'root'
})
export class TrackerService {
	constructor (private http: HttpAuth, private translate: TranslateService, private loginutils: LoginUtils) {}
	public options = {
		sizes: [
			{label: 5, value: 5},
			{label: 10, value: 10},
			{label: 20, value: 20},
			{label: 50, value: 50}
		],
	};


	getOrders(filters = {}): Observable<any> {
		if (filters['page']) filters['page']--;
		if (this.loginutils.user.role === 'ADMIN')	return this.http.get('/orders', filters).pipe(shareReplay());
		if (this.loginutils.user.role === 'ABSTRACTOR')	return this.http.get('/orders/abstractor-list', filters).pipe(shareReplay());
	}
}
