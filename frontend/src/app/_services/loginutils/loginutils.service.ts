import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { Logger } from '@wuja/logger';
import { Router } from '@angular/router';
import * as _ from 'lodash';
import { TranslateService } from '@ngx-translate/core';
import { LogoutService } from './logout.service';
import {HttpAuth} from '../httpauth/http-auth.service';
import { Storage } from '../storage/storage.service';
import {environment} from '../../../environments/environment';
import {UtilsService} from '../utils/utils.service';

export enum ROLES {USER, ADMIN}

@Injectable()
export class LoginUtils {
	public user$ = new Subject();
	private userSession;
	public roles;

	constructor( private log: Logger,
				private storage: Storage,
				private translate: TranslateService,
				private logoutservice: LogoutService,
				private http: HttpAuth,
				private utils: UtilsService,
				private router: Router ) {
		this.log.init( 'LoginUtils', this );
		this.storage.get( 'user' ).subscribe( user => this.user = user );
		this.checkLogout();
		this.roles = this.utils.listFromEnum(ROLES);
	}

	public checkLogout () {
		this.logoutservice.status.subscribe(() => this._logout());
	}

	public get user() {
		return this.userSession;
	}

	public set user( user ) {
		this.storage.set( 'user', user )
			.subscribe( res => {
				// this.log.log('return from storage', res);
				this.userSession = user;
				/* future development */
				this.emitUserChanges();
			} );
	}

	public get isLogged () {
		return !_.isEmpty(this.userSession) && this.userSession;
	}

	private emitUserChanges() {
		this.user$.next( this.userSession );
	}

	private _logout() {
		const logout = () => this.storage.delete( 'user' ).subscribe( sres => {
			this.router.navigate( [ '/login' ] );
			this.userSession = null;
		} );
		this.http.post('/logout', {}).subscribe(logout, logout);
	}

	useLang (lang) {
		const langsMatch = environment.availableLangs ? new RegExp(environment.availableLangs.join('|'), 'gi') : /en/;
		const defaultLang = environment.language;
		let outputLang = defaultLang;
		if (lang && lang.match(langsMatch)) {
			this.translate.use(lang);
			this.log.log(`language set by user > ${lang}`);
			outputLang = lang;
		} else {
			const browserLang = this.translate.getBrowserLang();
			const cond = browserLang.match(langsMatch) ? browserLang : defaultLang;
			this.translate.use(cond);
			this.log.log(`language set by default > ${cond}`);
			outputLang = cond;
		}

		this.storage.set('lang', outputLang).subscribe(res => {});
	}

	isRoleSufficient(roles: string | string[]) {
		const userRoles = [this.user.role];
		const askedRoles = typeof roles !== 'object' ? [roles] : roles;
		const contains = userRoles.filter(item => askedRoles.indexOf(item) > -1);
		return !!contains.length;
	}

	isPermitted (perms: string[]): boolean {
		const currentPerms = this.user.permissions || [this.user.role];
		const compare = currentPerms.filter(item => perms.includes(item));
		return (compare && compare.length) ? !!compare.length : false;
	}
}
