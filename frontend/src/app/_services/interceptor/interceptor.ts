import {Injectable} from '@angular/core';
import {HttpEvent, HttpInterceptor, HttpHandler, HttpRequest} from '@angular/common/http';
import { Observable } from 'rxjs';
import { Logger } from '@wuja/logger';
import { environment } from '../../../environments/environment';

@Injectable()
export class Interceptor implements HttpInterceptor {
	private API_KEY = '~api';
	constructor (private log: Logger) {}
	recreateUrl (url) {
		let newurl = '';
		if (url.match(this.API_KEY)) newurl = url.replace(new RegExp(this.API_KEY, 'gi'), environment.apiUrl);
		else newurl = url;
		return newurl;
	}

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		const changedReq = req.clone({
			// headers: req.headers.set('My-Header', 'MyHeaderValue'),
		});
		return next.handle(changedReq);
	}
}
