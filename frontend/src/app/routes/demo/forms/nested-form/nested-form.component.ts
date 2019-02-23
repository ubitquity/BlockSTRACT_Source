import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { Logger } from '@wuja/logger';

@Component( {
	selector: 'app-nested-form',
	template: `
		<form [formGroup]="nestedForm" (ngSubmit)="onSubmit(nestedForm)">

			<app-form-field [form]="nestedForm" field="name" [inlineErrors]="validationMessages">
				<div class="form-group">
					<label for="name">name*:</label>
					<input class="form-control" id="name" name="name" formControlName="name">
				</div>
			</app-form-field>

			<fieldset formGroupName="address">

				<app-form-field [form]="nestedForm" field="address.street" [inlineErrors]="validationMessages">
					<div class="form-group">
						<label for="street">street*:</label>
						<input class="form-control" id="street" name="street" formControlName="street">
					</div>
				</app-form-field>

				<div class="form-group">
					<label for="city">city:</label>
					<input class="form-control" id="city" name="city" formControlName="city">
				</div>

			</fieldset>
			<button class="btn" [disabled]="!nestedForm.valid" type="submit">Send</button>

		</form>

		<pre class="mb-0 p-3 mt-3 bg-light">{{ nestedForm.value | json }}</pre>
	`,
	styles: []
} )
export class NestedFormComponent implements OnInit {

	nestedForm: FormGroup;

	public validationMessages = {
		name: {
			required: 'Name is required.',
			pattern: 'The first character should be uppercase.'
		},
		address: {
			street: {
				required: 'Street is required.',
				minlength: 'The street name is too short.',
			}
		}
	};

	constructor( private formBuilder: FormBuilder, private log: Logger ) {}

	ngOnInit() {
		this.nestedForm = this.formBuilder.group( {
			name: [ '', [ Validators.required, Validators.pattern( '^[A-Z](.*)$' ) ] ],
			address: this.formBuilder.group( {
				street: [ '', [ Validators.required, Validators.minLength( 3 ) ] ],
				city: 'Bydgoszcz'
			} )
		} );
	}

	onSubmit( form ) {
		this.log.log( form );
	}
}
