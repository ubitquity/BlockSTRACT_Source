import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'app-confirm-modal-component',
	template: `<div class="py-4 px-3 text-center card">
		<h4 class="">{{titleText}}</h4>
		<p class="mt-3 mb-4">{{text}}</p>
		<div class="d-flex mx-auto">
			<button class="btn btn-success btn-lg mr-2" (click)="activeModal.close('ok = close')">{{'common.ok' | translate}}</button>
			<button class="btn btn-secondary btn-lg" (click)="activeModal.dismiss('cancel = dismiss')">{{'common.cancel' | translate}}</button>
		</div>
	</div>`,
	styles: [`
		button {
			min-width: 90px;
		}
	`]
})

export class ConfirmModalComponent {
	public text;
	public titleText;
	constructor(public activeModal: NgbActiveModal) {}
}
