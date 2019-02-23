import { Pageable } from './utils.service';
import { Injectable } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import * as _ from 'lodash';
import { Logger } from '@wuja/logger';
import { FormControl, ValidatorFn } from '@angular/forms';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import * as moment from 'moment';

@Injectable()
export class UtilsService {
	private previousUrl: string;
	private currentUrl: string;

	constructor(private log: Logger, private router: Router) {
		this.currentUrl = this.router.url;
		router.events.subscribe(event => {
			if (event instanceof NavigationEnd) {
				this.previousUrl = this.currentUrl;
				this.currentUrl = event.url;
			}
		});
	}

	public getPreviousUrl() {
		return this.previousUrl;
	}

	columnsForDatatables(columns: any[] = []) {
		if (!columns.length) return columns;
		return columns.map(this.columnsCreator);
	}

	private columnsCreator(item) {
		return typeof item === 'string'
			? { name: item }
			: _.has(item, 'name')
			? item
			: { name: 'no-name' };
	}

	listFromEnum(enm, onlyKeys = true) {
		this.log.log('meh', enm);
		return onlyKeys
			? Object.keys(enm).filter(k => typeof enm[k as any] === 'number')
			: Object.keys(enm)
					.filter(k => typeof enm[k as any] === 'number')
					.map(item => ({ [enm[item]]: item }));
	}

	public matchOtherValidator (otherControlName: string) {

		let thisControl: FormControl;
		let otherControl: FormControl;

		return function matchOtherValidate (control: FormControl) {

		  if (!control.parent) {
			return null;
		  }

		  // Initializing the validator.
		  if (!thisControl) {
			thisControl = control;
			otherControl = control.parent.get(otherControlName) as FormControl;
			if (!otherControl) {
			  throw new Error('matchOtherValidator(): other control is not found in parent group');
			}
			otherControl.valueChanges.subscribe(() => {
			  thisControl.updateValueAndValidity();
			});
		  }

		  if (!otherControl) {
			return null;
		  }

		  if (otherControl.value !== thisControl.value) {
			return {
			  matchOther: true
			};
		  }

		};
	}

	public noWhitespaceValidator(control: FormControl) {
		if (!control.value) return;
		const isWhitespace = (String(control.value)).trim().length === 0;
		const isValid = !isWhitespace;
		return isValid ? null : { 'whitespace': true };
	}


	public datepickerToTimestamp(datepicker: NgbDateStruct | null): number | null {
		if (datepicker == null) {
			return null;
		}
		const m: moment.Moment = moment([datepicker.year, datepicker.month - 1, datepicker.day]);
		return m.valueOf();
	}

	public zeroValidator: ValidatorFn = (control: FormControl) => {
		if (control.value <= 0 && control.value !== null) {
			return {
				zeroValue: { value: 'value lower than 0' }
			};
		}
		return null;
	}
}

export interface DTColumn {
	name: string;
}

export interface Pageable {
	page?: number;
	size?: number;
	sort?: string;
	ascending?: boolean;
}

export class PageableSort {
	readonly property: string;
	readonly ascending: boolean;
	readonly requestParam: string;

	constructor(property: string, ascending = true) {
		this.property = property;
		this.ascending = ascending;
		this.requestParam = property + ',' + (ascending ? 'asc' : 'desc');
	}
}
