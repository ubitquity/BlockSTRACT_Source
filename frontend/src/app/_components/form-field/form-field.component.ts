import { ChangeDetectionStrategy, Component, DoCheck, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { Form, FormGroup } from '@angular/forms';
import { Logger } from '@wuja/logger';

@Component( {
	selector: 'app-form-field',
	template: `
		<ng-content></ng-content>
		<app-inline-errors [form]="_form" [fieldName]="_field" [errorMessages]="_inlineErrors"></app-inline-errors>
		<app-server-errors [errors]="_serverErrors" [fieldName]="_field" *ngIf="_serverErrors"></app-server-errors>
	`,
} )
export class FormFieldComponent {

	public _serverErrors = null;
	public _field: string = null;
	public _form: FormGroup;
	public _inlineErrors: {};

	@Input() set form (form: FormGroup) {
		if (!form) return;
		this._form = form;
	}
	@Input() set serverErrors (errors) {
		if (!errors) return;
		this._serverErrors = errors;
	}
	@Input() set field(field: string) {
		if (!field) return;
		this._field = field;
	}
	@Input() set inlineErrors(err: {}) {
		if (!err) return;
		this._inlineErrors = err;
	}

	constructor(private log: Logger) {}
}
