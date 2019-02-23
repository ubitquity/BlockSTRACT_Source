import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InboxService } from '../inbox.service';
import { Logger } from '@wuja/logger';
import { ToastrService } from 'ngx-toastr';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';

@Component( {
	selector: 'app-new-message-modal',
	template: `<div class="py-4 px-3 text-center card" *ngIf="list?.length">
	<ng-container *ngIf="user.role === 'ADMIN'">
		<h4 class="">Choose Abstractor</h4>
		<p class="mt-3 mb-4">Choose an Abstractor you want to chat with.</p>
	</ng-container>
	<ng-container *ngIf="user.role === 'ABSTRACTOR'">
		<h4 class="">Choose Order</h4>
		<p class="mt-3 mb-4">Choose an Order you want to chat about.</p>
	</ng-container>

	<ng-select [items]="list"
			class="text-left"
			bindLabel="name"
			bindValue="id"
			[(ngModel)]="chosenId"
			(ngModelChange)="modal.close(chosenId)">
		</ng-select>
</div>
<div class="py-4 px-3 text-center card" *ngIf="!receivers">
	<h5 class="mb-0">You don't have any active orders</h5>
</div>
`
} )
@AutoUnsubscribe()
@LogInit()
export class NewMessageModalComponent implements OnInit {
	public errors;
	public user;
	public list;
	public receivers = true;

	public chosenId;


	@Output()
	finished: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor(private service: InboxService,
				private log: Logger,
				private toastr: ToastrService,
				public modal: NgbActiveModal,
				public loginutils: LoginUtils
				) {
	}

	ngOnInit() {
		this.user = this.loginutils.user;
		this.getList();
	}

	getList () {
		if (this.user.role === 'ADMIN') {
			this.service.getListOfAbstractors().subscribe(res => {
				this.list = res.list;
				this.receivers = res.list.length > 0 ? true : false;
			});
		}
		if (this.user.role === 'ABSTRACTOR') {
			this.service.getActiveOrdersList().subscribe(res => {
				this.list = res.list;
				this.receivers = res.list.length > 0 ? true : false;
			});
		}
	}

}
