import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Logger } from '@wuja/logger';
import {AutoUnsubscribe} from '../../../../_decorators/autounsub';
import {LogInit} from '../../../../_decorators/loginit';
import {faAngleLeft} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../../_services/utils/utils.service';
import { ActivatedRoute, Router } from '@angular/router';
import { OrdersService } from '../../orders.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmModalComponent } from '../../../../_components/confirm-modal/confirm-modal.component';
import { LoginUtils } from '../../../../_services/loginutils/loginutils.service';
import { RateModalComponent } from '../../../../_components/rate-modal/rate-modal.component';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';


@Component( {
	selector: 'app-overview',
	templateUrl: './overview.component.html'
} )

@AutoUnsubscribe()
@LogInit()
export class OverviewComponent implements OnInit {

	public faAngleLeft = faAngleLeft;
	public id;
	public prevUrl;
	public user;
	@Input() order;
	@Output() refresh = new EventEmitter<any>();

	constructor(private log: Logger,
		private utils: UtilsService,
		private route: ActivatedRoute,
		private router: Router,
		private modalService: NgbModal,
		private location: Location,
		private loginutils: LoginUtils,
		private toastr: ToastrService,
		private service: OrdersService) {
		this.id = this.route.snapshot.params.id;
}

	ngOnInit() {
		this.location.go(`/orders/details/${this.id}/overview`);
		this.prevUrl = this.utils.getPreviousUrl();
		this.user = this.loginutils.user;
	}

	manageOrder (choice: string) {
		const instance = this.modalService.open(ConfirmModalComponent, {windowClass: 'transparent'});
		switch (choice) {
			case 'accept': {
				instance.componentInstance.text = 'Are you sure you want to accept this order?';
				instance.componentInstance.titleText = 'Accept Order';
				instance.result.then(ok => {
					this.service.acceptOrder(this.id).subscribe(res => {
						this.toastr.success('The order has been accepted!');
						this.refresh.emit();
					}, err => {
						if (err.status === 409) {
							this.toastr.error('This order is already taken!');
						} else {
							this.toastr.error('Error during order acceptance!');
						}
					});

				}, cancel => {
					return;
				});
				break;
			}
			case 'decline': {
				instance.componentInstance.text = 'Are you sure you want to decline this order?';
				instance.componentInstance.titleText = 'Decline Order';
				instance.result.then(ok => {
					this.service.declineOrder(this.id).subscribe(res => {
						this.toastr.warning('The order has been declined!');
						this.router.navigate(['/orders/list']);
						this.refresh.emit();
					}, err => {
						this.toastr.error('Error while declining the order! ');

					});
				}, cancel => {
					return;
				});
				break;
			}
			case 'approve': {
				instance.componentInstance.text = 'Are you sure you want to approve this order?';
				instance.componentInstance.titleText = 'Approve Order';
				instance.result.then(ok => {
					this.log.log('order approved!');
					this.rateAbstractor('accept');
					}, cancel => {
					return;
				});
				break;
			}
			case 'incomplete': {
				instance.componentInstance.text = 'Are you sure you want to resend this order to abstractor?';
				instance.componentInstance.titleText = 'Resend to Abstractor';
				instance.result.then(ok => {
					this.log.log('order incomplete!');
					this.service.setAsIncomplete(this.id).subscribe(res => {
						this.refresh.emit();
					});

				}, cancel => {
					return;
				});
				break;
			}
			case 'recall': {
				instance.componentInstance.text = 'Are you sure you want to recall this order from this abstractor?';
				instance.componentInstance.titleText = 'Recall Order';
				instance.result.then(ok => {
					this.rateAbstractor('recall');
				}, cancel => {
					return;
				});
				break;
			}
			case 'cancel': {
				instance.componentInstance.text = 'Are you sure you want to cancel this order?';
				instance.componentInstance.titleText = 'Cancel Order';
				instance.result.then(ok => {
					this.service.cancel(this.id).subscribe(res => {
						this.refresh.emit();
					}, res => {
						this.toastr.error('Error during canceling order');
					});
				}, cancel => {
					return;
				});
				break;
			}
			case 'paid': {
				instance.componentInstance.text = 'Are you sure you already paid for this order?';
				instance.componentInstance.titleText = 'Confirm Payment';
				instance.result.then(ok => {
					this.service.setAsPaid(this.id).subscribe(res => {
						this.refresh.emit();
					});
				}, cancel => {
					return;
				});
				break;
			}
		}
	}

	rateAbstractor(action) {
		const instance = this.modalService.open(RateModalComponent,  {windowClass: 'transparent', backdrop: 'static', keyboard: false});
		instance.componentInstance.force = true;
		instance.result.then(
			res => {
				const rate = {'rate': res};
				if (action === 'recall') {
					this.service.recall(this.id, rate).subscribe(result => {
						this.refresh.emit();
					}, err => {});
				}
				if (action === 'accept') {
					this.service.setAsAccepted(this.id, rate).subscribe(result => {
						this.refresh.emit();
					}, err => {});
				}


			}, close);
	}

}
