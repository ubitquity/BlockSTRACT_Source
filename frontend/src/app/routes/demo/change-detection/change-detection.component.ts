import {Component, OnInit} from '@angular/core';

@Component({
	selector: 'app-change-detection-component',
	template: `
		<app-header name="Change detection demo"></app-header>
		<div class="card mb-3">
			<div class="card-body">
				<h3 class="mt-0 mb-3"></h3>
				<p>Change detection default</p>
				<app-cd-default></app-cd-default>
			</div>
		</div>
	`
})

export class ChangeDetectionComponent implements OnInit {
	constructor() {
	}

	ngOnInit() {
	}
}
