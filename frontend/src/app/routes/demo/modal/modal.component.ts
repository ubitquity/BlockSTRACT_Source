import {Component, OnInit} from '@angular/core';
import {ModalDismissReasons, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ModalModalComponent} from './modal-modal/modal-modal.component';
import {Logger} from '@wuja/logger';
import {ConfirmModalComponent} from '../../../_components/confirm-modal/confirm-modal.component';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';

@Component({
	selector: 'app-modal',
	template: `
		<ng-template #content let-c="close" let-d="dismiss">
			<div class="modal-header">
				<h4 class="modal-title">Modal title</h4>
				<button type="button" class="close" aria-label="Close" (click)="d('Cross click')">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<div class="modal-body">
				<p>One fine body&hellip;</p>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-outline-dark" (click)="c('Close click')">Close</button>
			</div>
		</ng-template>
		
		
		<app-header name="Modal" class="no-border"></app-header>
		<div class="card mb-3">
			<div class="card-body">
				<h3 class="mt-0 mb-5">Modal na podstawie komponentu</h3>
				<p>w poniższym przykładzie przekazujemy dane wstrzykując je do komponentu</p>
				<ng-select [items]="modalColors" 
						   bindLabel="name" 
						   [searchable]="false" 
						   [clearable]="false"
						   class="pull-left mr-3" 
						   [(ngModel)]="colorsSelected"></ng-select>
				<button class="btn btn-primary mr-3 pull-right" (click)="openModal1()">open modal #1</button>
			</div>
		</div>

		<div class="card mb-3">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Modal inline</h3>
				<p>modal ładowany i generowany bezpośrednio z widoku</p>
				<button class="btn btn-primary" (click)="open(content)">Open modal #2</button>
				<pre class="p-3 bg-light">{{closeResult}}</pre>
			</div>
		</div>
		
		<div class="card mb-3">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Confirm modal</h3>
				<p>Modal z dwoma przyciskami; Wartość kliknięcia z modala wraca do komponentu</p>
				<button class="btn btn-primary" (click)="openConfirmModal()">Open confirm modal</button>
			</div>
		</div>
	`
})
@AutoUnsubscribe()
@LogInit()
export class ModalComponent {

	closeResult: string;

	someData1 = {value: 1, other: 'value1'};
	modalColors = [
		{name: 'szary', value: 'bg-light'},
		{name: 'zielony', value: 'bg-success text-white'},
		{name: 'czerwony', value: 'bg-danger text-white'},
		{name: 'primary', value: 'bg-primary text-white'}
	];
	colorsSelected = this.modalColors[0];

	constructor(private modalService: NgbModal, private log: Logger) {}

	openModal1 () {
		const instance = this.modalService.open(ModalModalComponent, {windowClass: 'transparent'});
		instance.componentInstance.someData = this.someData1;
		instance.componentInstance.bg = this.colorsSelected;
		instance.result.then(
			res => {}, close => {}
		);
	}

	openConfirmModal () {
		const options = {windowClass: 'transparent'};
		const instance = this.modalService.open(ConfirmModalComponent, options);

		instance.result.then(
			res => this.log.log('ok', res),
			res => this.log.log('cancel', res)
		);
	}

	open(content) {
		this.modalService.open(content).result.then((result) => {
			this.closeResult = `Closed with: ${result}`;
		}, (reason) => {
			this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
		});
	}

	private getDismissReason(reason: any): string {
		if (reason === ModalDismissReasons.ESC) {
			return 'by pressing ESC';
		} else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
			return 'by clicking on a backdrop';
		} else {
			return  `with: ${reason}`;
		}
	}

}
