
import {debounceTime} from 'rxjs/operators';
import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {Logger} from '@wuja/logger';


@Component({
	selector: 'app-simple-form',
	template: `
		<form [formGroup]="simpleForm" (submit)="onSubmit($event)">
			<div class="form-group">
				<label for="email">email*:</label>
				<input class="form-control" id="email" name="email" type="email" formControlName="email">
				<!-- przykład walidacji inlinowej na podstawie pola branego bezpośrednio z formularza (przydatne głównie w pętlach)-->
				<p class="text-danger" *ngIf="simpleForm.get('email').invalid">
					<span *ngIf="simpleForm.get('email').errors['email'] && simpleForm.get('email').dirty">
						no email
					</span>
					<span *ngIf="simpleForm.get('email').errors['required'] && simpleForm.get('email').dirty">
						required
					</span>
				</p>
			</div>

			<div class="form-group">
				<label for="age">age:</label>
				<input class="form-control" type="number" id="age" name="age" formControlName="age">
				<p class="text-danger" *ngIf="simpleForm.get('age').invalid">
					<span *ngIf="simpleForm.get('age').errors['min'] && simpleForm.get('age').dirty">
						age is too low
					</span>
					<span *ngIf="simpleForm.get('age').errors['max'] && simpleForm.get('age').dirty">
						age is too high
					</span>
				</p>
			</div>

			<div class="form-group">
				<label for="country">country*:</label>
				<input class="form-control" id="country" name="country" formControlName="country">
				<p class="text-danger" *ngIf="simpleForm.get('country').invalid">
					<span *ngIf="simpleForm.get('country').errors['required'] && simpleForm.get('country').dirty">required</span>
					<span *ngIf="simpleForm.get('country').errors['pattern'] && simpleForm.get('country').dirty">pattern</span>
					<span *ngIf="simpleForm.get('country').errors['minlength'] && simpleForm.get('country').dirty">too short</span>
				</p>
				<!--<p *ngIf="simpleFormErrors.country" class="text-danger">{{ simpleFormErrors.country }}</p>-->
			</div>

			<button class="btn btn-success" [disabled]="simpleForm.invalid" type="submit">Send</button>

		</form>

		<pre class="bg-light mb-0 mt-3 p-3">{{ simpleForm.value | json }}</pre>
	`,
	styles: []
})
export class SimpleFormComponent implements OnInit {
	simpleForm: FormGroup;

	constructor(private formBuilder: FormBuilder, private log: Logger) {}

	ngOnInit() {
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
