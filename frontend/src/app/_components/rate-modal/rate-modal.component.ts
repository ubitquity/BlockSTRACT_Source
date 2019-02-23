import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ConnectionService } from 'ng-connection-service';

@Component({
	selector: 'app-rate-modal-component',
	template: `
		<div class='py-4 px-3 text-center card'>
			<h4>Rate Abstractor</h4>
			<p class='mt-3 mb-2'>
			How do you rate your experience with this abstractor?
			</p>
			<ngb-rating class='mx-auto mb-4'	[(rate)]='rate' max='5'
			>
				<ng-template let-fill='fill' let-index='index'>
					<span class='star medium' [class.filled]='fill === 100'
						>&#9733;</span
					>
				</ng-template>
			</ngb-rating>
			<div class='d-flex mx-auto'>
				<button
					[disabled]="!rate"
					class='btn btn-success btn-lg mr-2'
					(click)='saveChanges()'
				>
					Save
				</button>
				<button
					*ngIf='!force'
					class='btn btn-secondary btn-lg'
					(click)="activeModal.dismiss('cancel = dismiss')"
				>
					Cancel
				</button>
			</div>
		</div>
	`,
	styles: [
		`
			button {
				min-width: 90px;
			}
		`
	]
})
export class RateModalComponent {
	force = false;
	public rate;

	constructor(public activeModal: NgbActiveModal) {}

	saveChanges() {
		this.activeModal.close(this.rate);
	}
}
