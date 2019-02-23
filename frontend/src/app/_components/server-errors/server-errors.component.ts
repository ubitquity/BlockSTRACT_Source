import { Component, Input, ViewContainerRef } from '@angular/core';
import { Logger } from '@wuja/logger';

@Component( {
	selector: 'app-server-errors',
	template: `
		<div class="bg-danger mt-1 mb-2 p-2 small rounded text-white" *ngIf="customError">
			{{ 'validation_errors.'+customError | translate }}
			{{details?.length ? details[0] + '-' + details[1] + ' characters long.' : null }}
		</div>
	`
} )
export class ServerErrorsComponent {

	public parent;
	public customError: string;
	private workingField: string;
	public details;

	constructor( private vcRef: ViewContainerRef, private log: Logger ) {
		this.parent = this.vcRef.parentInjector[ 'view' ].component;
	}

	@Input() set fieldName (field: string) { this.workingField = field; }
	@Input()
	set errors(errors: any) {
		const p = e => e.field === this.workingField;
		this.customError = (errors && errors.find(p)) ? errors.find(p).message : null;
		if (this.customError === 'INVALID_SIZE') this.details = (errors && errors.find(p)) ? errors.find(p).parameters : null;

	}
}
