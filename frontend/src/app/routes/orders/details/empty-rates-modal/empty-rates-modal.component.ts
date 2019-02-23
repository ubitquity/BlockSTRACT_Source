import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'app-empty-rates-modal-component',
	template: `<div class="py-4 px-3 text-center card">
		<h4 class="">You don't have Abstract Rates</h4>
		<p class="mt-3 mb-4">Before you accept the order, please add Abstract Rates to your profile and set up your prices.
		</p>
		<div class="d-flex mx-auto">
			<button class="btn btn-success btn-lg mr-2" (click)="activeModal.close('ok = close')">Go to Profile</button>
			<button class="btn btn-secondary btn-lg" (click)="activeModal.dismiss('cancel = dismiss')">{{'common.cancel' | translate}}</button>
		</div>
	</div>`,
	styles: [`
		button {
			min-width: 90px;
		}
	`]
})

export class EmptyRatesComponent {
	constructor(public activeModal: NgbActiveModal) {}
}
