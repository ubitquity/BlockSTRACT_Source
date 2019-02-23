import {Component, OnInit} from '@angular/core';
import {Validators, FormControl} from '@angular/forms';
import {Logger} from '@wuja/logger';

@Component({
	selector: 'app-single-input',
	template: `
		<div class="form-group">
			<label for="singleInput">max 3 characters:</label>
			<input id="singleInput" class="form-control" type="search" name="singleInput" [formControl]="singleInput">
			<p *ngIf="singleInput.invalid && singleInput.dirty" class="pt-1">write a max of 3 characters!</p>
		</div>

		<pre class="bg-light mb-0 p-3">{{ singleInput.value | json }}</pre>
	`,
	styles: []
})
export class SingleInputComponent implements OnInit {

	singleInput = new FormControl('', Validators.maxLength(3));

	constructor(private log: Logger) {
	}

	ngOnInit() {
		this.singleInput.valueChanges.subscribe(value => {
			this.log.log('single input valueChanges', value);
		});
	}
}
