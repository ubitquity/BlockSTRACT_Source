
import {throwError as observableThrowError, Observable} from 'rxjs';

import {catchError, tap} from 'rxjs/operators';
import {Injectable, Inject} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';


import {Logger} from '@wuja/logger';
import * as _ from 'lodash';
import {ErrorsService} from '../errors/errors.service';
import {environment} from '../../../environments/environment';

export interface JSONBodyParams {
	[key: string]: any;
}

interface HttpOptions {
	params?: HttpParams;
	headers: HttpHeaders;
	body?: any;
}

export interface HttpclientSettings {
	app: {
		predefToken: string|null;
		apiUrl: string,
		authorizationHeaderName: string,
		storageTokenLocation: string
	};
}

export interface HttpAuthConfig {
	storagePrefix: string;
	predefToken: string;
	apiUrl: string;
	authorizationHeaderName: string;
	storageTokenLocation: string;
}

export interface HttpErrorHandler {
	handleError(err: HttpErrorResponse): any;
}

@Injectable()
export class HttpAuth {
	public headers: HttpHeaders = new HttpHeaders();
	private conf = environment;

	constructor(private _rules: ErrorsService,
				private http: HttpClient,
				private log: Logger) {
	}

	private get token() {
		let token = null;
		if (this.conf.predefToken) {
			this.log.event('warning !', 'using predefined token - change this in environment.ts');
			token = this.conf.predefToken;
		} else {
			if (window.localStorage) {
				const maybeJSON = (jsonString) => {
					try {
						const o = JSON.parse(jsonString);
						if (o && typeof o === 'object') {
							return o;
						}
					} catch (e) {}

					return null;
				};
				const obj = {};
				Object.keys(window.localStorage).filter(item => item.match(this.conf.storagePrefix))
					.forEach(key => obj[key.split(`${this.conf.storagePrefix}-`)[1]] = maybeJSON(window.localStorage.getItem(key)));

				token = _.has(obj, this.conf.storageTokenLocation) ?
					_.get(obj, this.conf.storageTokenLocation) :
					null;
			} else token = null;
		}
		return token;
	}

	private get apiurl() {
		return this.conf.apiUrl || '/';
	}

	public request<T>(method: 'GET'|'POST'|'PUT'|'DELETE'|'PATCH',
					  url: string,
					  params?: JSONBodyParams,
					  body?: any) {
		const fullUrl = this.geturl(url);
		const options: HttpOptions = {
			headers: this.createHeaders()
		};
		if (params) options.params = this.getParams(params);
		if (body) options.body = body;
		return this.http.request<T>(method, fullUrl, options).pipe(
			tap(res => this.log.request(method, fullUrl, res)),
			catchError((err: HttpErrorResponse) => {
				this.log.error(`${method}: ${err.status} `, fullUrl, err, this);
				return this._rules ? this._rules.handleError(err) : observableThrowError(err);
			}));
	}

	public get<T>(url: string, params: JSONBodyParams = {}) {
		return this.request<T>('GET', url, params);
	}

	public post<T>(url: string, body: JSONBodyParams = {}) {
		return this.request<T>('POST', url, null, body);
	}

	public put<T>(url: string, body: JSONBodyParams = {}) {
		return this.request<T>('PUT', url, null, body);
	}

	public patch<T>(url: string, params: JSONBodyParams = {}) {
		return this.request<T>('PATCH', url, params);
	}

	public delete<T>(url: string, params: JSONBodyParams = {}) {
		return this.request<T>('DELETE', url, params);
	}

	private geturl(url) {
		return url.charAt(0) === '!' ? url.substr(1, url.length) : `${this.apiurl}${url}`;
	}

	private createHeaders() {
		this.headers = this.headers.set('Accept', 'application/json');
		if (this.token) {
			this.headers = this.headers.set(this.conf.authorizationHeaderName, this.token);
		}
		return this.headers;
	}

	private getParams(params: any): HttpParams {
		let newParams = new HttpParams();
		// _.forEach(params, (value, key) => newParams = newParams.append('' + key, value));
		_.forEach(params, (value, key) => {
			newParams = newParams.append('' + key, value);
			// this.log.log('creating params', key, value);
		});
		// this.log.log('params serialized', newParams.toString());
		return newParams;
	}
}

// export const HttpAuthProvider = (conf: any, rules: HttpErrorHandler, http, log) => new HttpAuth(rules, http, log);

// export const HttpAuthProvider =
// (http: HttpClient, log: Logger, storage: Storage, settings: HttpclientSettings, rules: HttpErrorHandler) => {
//     return new HttpAuth(http, log, storage, settings, rules);
// };
