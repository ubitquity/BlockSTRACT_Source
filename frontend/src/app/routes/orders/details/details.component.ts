import { Component, OnInit, Input } from '@angular/core';
import { Logger } from '@wuja/logger';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import { faAngleLeft, faSearch, faCaretDown } from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../_services/utils/utils.service';
import { ActivatedRoute, Router } from '@angular/router';
import { OrdersService } from '../orders.service';
import { Location } from '@angular/common';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';
import { ToastrService } from 'ngx-toastr';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EmptyRatesComponent } from './empty-rates-modal/empty-rates-modal.component';


@Component( {
	selector: 'app-details',
	templateUrl: './details.component.html',
	styleUrls: ['./details.component.scss']
} )

@AutoUnsubscribe()
@LogInit()
export class DetailsComponent implements OnInit {

	public faAngleLeft = faAngleLeft;
	public faSearch = faSearch;
	public faCaretDown = faCaretDown;
	public id;
	public currentTab = '';
	public prevUrl;
	public search = '';
	public order;
	public user;

	constructor(private log: Logger,
		private utils: UtilsService,
		private location: Location,
		private route: ActivatedRoute,
		private router: Router,
		private loginutils: LoginUtils,
		private modalService: NgbModal,
		private toastr: ToastrService,
		private service: OrdersService) {
		this.id = this.route.snapshot.params.id;
		this.currentTab = this.route.snapshot.params.tab || 'overview';
}

	ngOnInit() {
		this.log.init('OrdersComponent / Details', this);
		this.prevUrl = this.utils.getPreviousUrl();
		this.user = this.loginutils.user;
		this.getOrder();
	}



	changeCurrentTab(target) {
		if (this.user.role === 'ABSTRACTOR' && this.order.internalStatus === 'UNCLAIMED' || this.user.role === 'ABSTRACTOR' && this.order.internalStatus === 'CANCELLED') return;
		this.currentTab = target;
		this.location.go(`/orders/details/${this.id}/${target}`);
	}

	getOrder() {
		this.service.getOrderDetails(this.id).subscribe(res => {
			this.order = res;
			window.scroll(0, 0);
			if (this.user.role === 'ABSTRACTOR' && this.order.ratesEmpty && this.order.internalStatus !== 'CANCELLED') {
				const instance = this.modalService.open(EmptyRatesComponent, {windowClass: 'transparent'});
				instance.result.then(
					result => this.router.navigate(['/profile']),
					close => {}
				);
			}
			// this.order.internalStatus = 'ABSTRACT_INCOMPLETE';
		}, err => {
			if (err.error._errorDetails === 'FORBIDDEN: ApiException: No access') {
				this.toastr.error('You don\'t have permission to access this order!');
			}
			this.router.navigate(['/orders/list']);
		});
	}
}
