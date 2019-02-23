
import {debounceTime} from 'rxjs/operators';
import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {Logger} from '@wuja/logger';


@Component({
	selector: 'app-simple-form-short',
	template: `
		<form [formGroup]="simpleForm" (submit)="onSubmit($event)">
			<app-form-field [form]="simpleForm" field="email">
				<div class="form-group">
					<label for="email">email*:</label>
					<input class="form-control" id="email" name="email" type="email" formControlName="email">
				</div>
			</app-form-field>

			<app-form-field [form]="simpleForm" field="age">
				<div class="form-group">
					<label for="age">age:</label>
					<input class="form-control" type="number" id="age" name="age" formControlName="age">
				</div>
			</app-form-field>

			<app-form-field [form]="simpleForm" field="country" [inlineErrors]="errorMessages">
				<div class="form-group">
					<label for="country">country*:</label>
					<input class="form-control" id="country" name="country" formControlName="country">
				</div>
			</app-form-field>

			<button class="btn btn-success" [disabled]="simpleForm.invalid" type="submit">Send</button>

		</form>

		<pre class="bg-light mb-0 mt-3 p-3">{{ simpleForm.value | json }}</pre>
	`,
	styles: []
})
export class SimpleFormShortComponent implements OnInit {
	simpleForm: FormGroup;

	errorMessages = {
		country: {
			minlength: 'there is no country with less than 4 letters'
		}
	};

	constructor(private formBuilder: FormBuilder, private log: Logger) {}

	ngOnInit() {
		this.log.init('SimpleFormComponent / Demo', this);
		this.simpleForm = this.formBuilder.group({
			email: ['', [Validators.required, Validators.email]],
			age: [18, [Validators.min(18), Validators.max(200)]],
			country: ['', [Validators.required, Validators.minLength(4), Validators.pattern('^[a-zA-Z]+$')]]
		});
		this.simpleForm.valueChanges.pipe(debounceTime(3000)).subscribe(res => {
			this.log.log('Message after 3s', res, this.simpleForm.controls);
		});
	}

	onSubmit($event) {
		$event.preventDefault();
		this.log.log('submit', this.simpleForm);
	}
}
