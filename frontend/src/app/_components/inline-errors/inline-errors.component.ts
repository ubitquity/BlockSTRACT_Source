import { ChangeDetectionStrategy, ChangeDetectorRef, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Logger } from '@wuja/logger';
import { TranslateService } from '@ngx-translate/core';
import * as _ from 'lodash';

@Component({
	selector: 'app-inline-errors',
	changeDetection: ChangeDetectionStrategy.OnPush,
	styles: [
		`:host:empty {display:none;}`
	],
	template: `
		<div class="text-danger mt-2"
			 *ngIf="_field?.errors && _field.dirty && _field.invalid"><div *ngFor="let e of errorKeys">{{ getErrorMessage(e) }}</div></div>`
})
export class InlineErrorsComponent implements OnInit {

	_form: FormGroup;
	_field: FormControl;
	_fieldName: string;
	_messages: {};
	public errorKeys = null;
	@Input() set form (form: FormGroup) {
		if (!form) return;
		this._form = form;
	}

	@Input() set fieldName (name) {
		this._fieldName = name;
	}

	@Input() set errorMessages (err: {}) {
		this._messages = err;
	}

	constructor(private log: Logger, private cd: ChangeDetectorRef, private translate: TranslateService) {
	}

	ngOnInit() {
		if (!this._form || !this._fieldName) return;
		this.cd.detach();
		this.cd.detectChanges();
		this._field = <FormControl>this._form.get(this._fieldName);
		this._field.valueChanges.subscribe(res => {
			this.errorKeys = Object.keys(this._field.errors || {});

			this.cd.detectChanges();
		});
	}

	getErrorMessage(key) {
		if (_.has(this._messages, this._fieldName) && !_.has(this._messages[this._fieldName], key)) {
			this.log.log(`no phrase for this error: ${key}`);
		}
		return _.has(this._messages, this._fieldName)
			? _.get(this._messages, `${this._fieldName}.${key}`)
			: this.translate.instant(`inlineErrors.${key}`);
	}
}
