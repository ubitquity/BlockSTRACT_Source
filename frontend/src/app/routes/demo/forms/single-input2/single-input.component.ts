import {Component, OnInit} from '@angular/core';
import {Validators, FormControl} from '@angular/forms';
import {Logger} from '@wuja/logger';

@Component({
	selector: 'app-single-input2',
	template: `
		<div class="form-group">
			<label for="singleInput">max 3 characters:</label>
			<input id="singleInput" 
				   class="form-control" 
				   type="search" 
				   name="singleInput" 
				   [(ngModel)]="singleInput">
		</div>

		<pre class="bg-light mb-0 p-3">{{ singleInput | json }}</pre>
	`,
	styles: []
})
export class SingleInput2Component implements OnInit {

	singleInput;

	constructor(private log: Logger) {
	}

	ngOnInit() {}

	inputChanges($event) {
		this.log.log('input changes', $event);
	}
}
