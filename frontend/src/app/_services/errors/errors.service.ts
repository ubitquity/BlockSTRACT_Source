
import {throwError as observableThrowError,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { Logger } from '@wuja/logger';
import * as _ from 'lodash';
import { ToastrService } from 'ngx-toastr';
import {ActivatedRoute, Router} from '@angular/router';
import { LogoutService } from '../loginutils/logout.service';

interface TheError extends HttpErrorResponse {
	id?: Symbol;
}

@Injectable()
export class ErrorsService  {
	// @TODO: Ten serwis powinien zawierać tylko i wyłącznie metody do obsługi błędów - reszta powinna dziedziczyć z @itcraft/httpclient
	errors: Map<Symbol, any> = new Map();
	url = new URL(location.href);

	// codes: Map<string | number, Function> = new Map();

	constructor( private log: Logger,
				 private logout: LogoutService,
				 private route: ActivatedRoute,
				 private toastr: ToastrService ) {
		// this.log.init('ErrorsService', this);
	}

	handleError( err: TheError ): Observable<any> {
		const id = Symbol( 'error' );
		err.id = id;
		this.errors.set( id, this.handler( err ) );
		return observableThrowError( this.errors.get( id ) );
	}

	handler( err: HttpErrorResponse ) {
		const method = `code${err.status}`;
		return err.status && !!this[ method ] ? this[ method ]( err ) : this.defaultHandler( err );
	}

	defaultHandler( err ) {
		const msg = err.status ? `No error code method implemented for ${err.status}` : `No error code`;
		this.log.error( msg, err );
		return err;
	}

	code400( err ) {
		try {
			const e = JSON.parse( err.error );
			this.toastr.error( e.errorDetails, e.error );
		} catch ( e ) {
		}
		return err;
	}

	code401( err ) {
		const split = err.url.split('/');
		if (split[split.length - 1] === 'login') return err;
		if (err.url && (split[split.length - 1] !== 'logout' && split[split.length - 1] !== 'logout')) this.logout.logout();
		// this.logout.logout();
		return err;
	}

	code409( err ) {
		try {
			const e = JSON.parse( err.error );
			// this.toastr.error(e.errorDetails, e.error);
		} catch ( e ) {
		}
		return err;
	}
}
