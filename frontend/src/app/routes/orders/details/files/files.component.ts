import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Logger } from '@wuja/logger';
import {AutoUnsubscribe} from '../../../../_decorators/autounsub';
import {LogInit} from '../../../../_decorators/loginit';
import { faAngleLeft, faCheckCircle, faMinusCircle } from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../../_services/utils/utils.service';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../orders.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmModalComponent } from '../../../../_components/confirm-modal/confirm-modal.component';
import { ToastrService } from 'ngx-toastr';
import { LoginUtils } from '../../../../_services/loginutils/loginutils.service';


@Component( {
	selector: 'app-files',
	templateUrl: './files.component.html',
	styleUrls: ['./files.component.scss']
} )

@AutoUnsubscribe()
@LogInit()
export class FilesComponent implements OnInit {

	public faAngleLeft = faAngleLeft;
	public faCheckCircle = faCheckCircle;
	public faMinusCircle = faMinusCircle;
	public id;
	public prevUrl;
	public icon = this.faMinusCircle;
	public file = {
		id: null
	};
	public pending = false;
	@Input() order;
	@Output() changeTab = new EventEmitter<any>();
	public user;
	public files;
	public pendingFiles;
	public waitForApproval;
	public showFiles = true;

	constructor(private log: Logger,
		private utils: UtilsService,
		private modalService: NgbModal,
		private toastr: ToastrService,
		private loginutils: LoginUtils,
		private route: ActivatedRoute,
		private service: OrdersService) {
		this.id = this.route.snapshot.params.id;
}

	ngOnInit() {
		this.user = this.loginutils.user;
		this.prevUrl = this.utils.getPreviousUrl();
		if (this.user.role !== 'ADMIN' && this.order.internalStatus === 'UNCLAIMED') {
			this.changeTab.emit('overview');
			return;
		}
		this.getFiles();
	}

	fileListener($event) {
		this.pendingFiles = $event;
		this.waitForApproval = true;
		this.showFiles = true;
	}

	saveFiles() {
		this.pending = true;
		this.service.postFiles(this.id, this.pendingFiles).subscribe(res => {
			this.getFiles();
		}, res => this.log.log('error', res));
	}

	cancelAdding() {
		this.waitForApproval = false;
		this.pendingFiles = null;
		if (!this.files.length) this.showFiles = false;
	}

	getFiles() {
		this.service.getOrderFiles(this.id).subscribe(res => {
			this.waitForApproval = false;
			this.pendingFiles = null;
			this.files = res;
			this.pending = false;

			this.showFiles = this.files.length ? true : false;
		}, err => this.showFiles = false);
	}

	deleteFile (fileId, $event) {
		$event.preventDefault();
		const instance = this.modalService.open(ConfirmModalComponent, {windowClass: 'transparent'});
		instance.componentInstance.text = 'Are you sure you want to delete this file?';
		instance.componentInstance.titleText = 'Delete File';
		instance.result.then(ok => {
			this.service.deleteFile(this.id, fileId)
				.subscribe(res => {
					this.toastr.success('Successfully deleted file');
					this.getFiles();
				});
		}, close => {
			return;
		});
	}

}
