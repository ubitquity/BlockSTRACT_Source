import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import {Logger} from '@wuja/logger';

@Component({
	selector: 'app-dynamic-validation',
	template: `
		<form class="model-form" [formGroup]="dynamicForm" (ngSubmit)="onSubmit(dynamicForm)">
			<div class="form-group">
				<p>subscribe for newsletter:</p>
				<div class="form-check form-check-inline">
					<label class="form-check-label">
						<input class="form-check-input" 
							   type="radio" 
							   name="subscription" 
							   value="email" 
							   formControlName="subscription"> email
					</label>
				</div>
				<div class="form-check form-check-inline">
					<label class="form-check-label">
						<input class="form-check-input" 
							   type="radio" 
							   name="subscription" 
							   value="sms" 
							   formControlName="subscription"> sms
					</label>
				</div>
			</div>

			<app-form-field [form]="dynamicForm" field="email" [inlineErrors]="validationMessages">
				<div class="form-group">
					<label for="email-2">
						email
						<span *ngIf="dynamicForm.get('subscription').value == 'email'" class="text-danger">*</span>:
					</label>
					<input id="email-2" class="form-control" name="email" formControlName="email">
				</div>
			</app-form-field>

			<app-form-field [form]="dynamicForm" field="phone" [inlineErrors]="validationMessages">
				<div class="form-group">
					<label for="phone">
						phone
						<span *ngIf="dynamicForm.get('subscription').value == 'sms'" class="text-danger">*</span>:
					</label>
					<input id="phone" class="form-control" name="phone" formControlName="phone">
				</div>
			</app-form-field>

			<div class="form-group">
				<div class="form-check form-check-inline">
					<label class="form-check-label">
						<input class="form-check-input" type="checkbox" name="cardPayment" formControlName="cardPayment"> I want to pay by card
					</label>
				</div>
			</div>

			<fieldset *ngIf="dynamicForm.get('cardPayment').value" formGroupName="card">

				<app-form-field [form]="dynamicForm" field="card.cardNumber" [inlineErrors]="validationMessages">
					<div class="form-group">
						<label for="card-number">Card number*:</label>
						<input id="card-number" class="form-control" name="cardNumber" formControlName="cardNumber">
					</div>
				</app-form-field>

				<app-form-field [form]="dynamicForm" field="card.expirationDate" [inlineErrors]="validationMessages">
					<div class="form-group">
						<label for="expiration-date">Expiration date*:</label>
						<input id="expiration-date" class="form-control" name="expirationDate" formControlName="expirationDate">
					</div>
				</app-form-field>

			</fieldset>

			<button class="btn btn-success" [disabled]="!dynamicForm.valid" type="submit">Send</button>
			<span>form is <code>{{ dynamicForm.valid ? 'valid' : 'invalid' }}</code></span>

		</form>

		<pre class="mt-3 mb-0 p-3 bg-light">{{ dynamicForm.value | json }}</pre>
	`,
	styles: []
})
export class DynamicValidationComponent implements OnInit {
	dynamicForm: FormGroup;
	public validationMessages = {
		email: {
			required: 'Email is required.',
			email: 'Enter the correct email address.'
		},
		phone: {
			required: 'Phone is required.',
		},
		cardNumber: {
			required: 'Card number is required.',
		},
		expirationDate: {
			required: 'Expiration Date is required.',
		}
	};

	constructor(private formBuilder: FormBuilder, private log: Logger) { }

	ngOnInit() {
		this.log.init('DynamicValidationComponent / Demo', this);
		this.dynamicForm = this.formBuilder.group({
			email: null,
			phone: null,
			subscription: 'sms',
			cardPayment: false,
			card: this.formBuilder.group({
				cardNumber: '',
				expirationDate: ''
			})
		});

		this.dynamicForm.get('subscription').valueChanges.subscribe(res => this.updateValidators());
		this.dynamicForm.get('cardPayment').valueChanges.subscribe(res => this.updateValidators());

		this.updateValidators();
	}

	updateValidators () {
		const subs = this.dynamicForm.get('subscription');
		const card = this.dynamicForm.get('cardPayment');
		const emailField = this.dynamicForm.get('email');
		const phoneField = this.dynamicForm.get('phone');
		const cardNumber = this.dynamicForm.get('card.cardNumber');
		const expirationDate = this.dynamicForm.get('card.expirationDate');
		const viaEmail = subs.value === 'email';
		const byCard = card.value;

		if (viaEmail) {
			emailField.setValidators([Validators.required]);
			phoneField.setValidators(null);
		} else {
			emailField.setValidators(null);
			phoneField.setValidators([Validators.required]);
		}

		phoneField.updateValueAndValidity();
		emailField.updateValueAndValidity();

		if (byCard) {
			cardNumber.setValidators([Validators.required]);
			expirationDate.setValidators([Validators.required]);
		} else {
			cardNumber.setValidators(null);
			expirationDate.setValidators(null);
		}

		cardNumber.updateValueAndValidity();
		expirationDate.updateValueAndValidity();

		this.dynamicForm.updateValueAndValidity();
	}

	onSubmit(form) {
		this.log.log(form);
	}

}
