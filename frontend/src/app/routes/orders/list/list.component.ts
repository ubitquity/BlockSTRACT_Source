import {AfterViewInit, Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { LogInit } from '@decorators/loginit';
import {OrdersService} from '../orders.service';
import {Logger} from '@wuja/logger';
import {Remember} from '@decorators/remember';
import {faSearch, faEllipsisH, faAngleLeft, faAngleRight} from '@fortawesome/free-solid-svg-icons';
import { TranslateService } from '@ngx-translate/core';
import { TableColumn } from '@swimlane/ngx-datatable';
import { UtilsService } from '../../../_services/utils/utils.service';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';
import { Router } from '@angular/router';


// @LogInit()
@Remember()
@Component({
	selector: 'app-list',
	templateUrl: './list.component.html'
})
export class ListComponent implements OnInit, AfterViewInit {
	faSearch = faSearch;
	faEllipsisH = faEllipsisH;
	faAngleLeft = faAngleLeft;
	faAngleRight = faAngleRight;
	public page = 1;
	public status = 'ALL';
	public pages = [];
	public size = {label: 10, value: 10};
	public sort = '';
	public totalPages;
	public totalElements;
	public currentTab = 'all';
	public tableColumns: TableColumn[];
	public user;
	public search = '';
	public selected = [];
	public showSearch;
	public filtered;
	@ViewChild('buttonsColumn') buttonsColumn: TemplateRef<any>;
	@ViewChild('statusColumn') statusColumn: TemplateRef<any>;
	@ViewChild('priceColumn') priceColumn: TemplateRef<any>;

	orders;
	showOrders = true;

	inClosingPrep;
	inOrderAcceptance;
	inPostClosing;
	inTitleProcessing;

	scrollBarHorizontal = (window.innerWidth < 1200);


	dtMessages = {
		emptyMessage: this.translate.instant('datatable.nodata'),
		totalMessage: this.translate.instant('datatable.total'),
		selectedMessage: this.translate.instant('datatable.selected')
	};

	public options = this.service.options;

	constructor(
		private log: Logger,
		private translate: TranslateService,
		private utils: UtilsService,
		private router: Router,
		private loginutils: LoginUtils,
		private service: OrdersService) {
			window.onresize = () => {
				this.scrollBarHorizontal = (window.innerWidth < 1200);
			  };
		}

	ngOnInit() {

		this.user = this.loginutils.user;
		if (this.user) this.getOrders();

		this.service.getOrdersStatuses().subscribe(res => {
			this.inClosingPrep = res.inClosingPrep;
			this.inOrderAcceptance = res.inOrderAcceptance;
			this.inPostClosing = res.inPostClosing;
			this.inTitleProcessing = res.inTitleProcessing;
		});
	}

	ngAfterViewInit() {
		this.tableColumns = this.utils.columnsForDatatables([
			{
				name: 'Order',
				prop: 'orderNumber'
			},
			{
				name: 'Status',
				prop: 'status',
				cellTemplate: this.statusColumn,
				maxWidth: 100
			},
			{
				name: 'Customer',
				prop: 'customerName'
			},
			{
				name: 'Product',
				prop: 'productName'
			},
			{
				name: 'Property Address',
				prop: 'flatAddress',
				width: 250
				},
			{
				name: 'Total Price',
				prop: 'quotedPrice',
				cellTemplate: this.priceColumn
			},
			{
				name: 'Due By',
				prop: 'dueDate'
			},

			{
				name: '',
				cellTemplate: this.buttonsColumn,
				cellClass: 'd-none d-lg-block edit',
				width: 50
			}
		]);
	}

	public toggleSearch() {
		this.showSearch = !this.showSearch;
		if (this.filtered && this.search) {
			this.filter({search: ''});
		}
		this.search = '';
		this.filtered = false;
	}

	public filter (filterObj: {[key: string]: any} = {}, $event = null): void {
		if ($event && $event.keyCode !== 13) return;
		Object.assign(this, filterObj);
		this.filtered = true;
		this.getOrders(true);
	}

	private getOrders (reset = false) {
		const { sort, status, search} = this;
		let page = this.page;
		if (reset) page = 0;
		const size = this.size.value;
		this.service.getOrders({page, status, search, size, sort})
			.subscribe(res => {
				this.orders = res;
				this.orders.content = res.content.map(element => ({
					...element,
					dueDate:  `${(new Date(element.dueDate).getMonth() + 1).toString().padStart(2, '0')}/${new Date(element.dueDate).getDate().toString().padStart(2, '0')}/${new Date(element.dueDate).getFullYear()}`
				}));
				this.page = res.number + 1;
				this.totalPages = res.totalPages;
				this.pages.length = 0;
				for (let i = 1; i <= res.totalPages; i++) {
					this.pages.push(i);
				}
				this.totalElements = res.totalElements;
				this.showOrders = this.orders.content.length ? true : false;

			}, res => {
				this.showOrders = false;
				this.orders = null;
			});
	}

	changeCurrentTab(target) {
		this.currentTab = target;
		switch (target) {
			case 'all':	this.status = ''; break;
			case 'open': this.status = 'OPEN'; break;
			case 'cancelled': this.status = 'CANCELLED'; break;
			case 'declined': this.status = 'DECLINED'; break;
			case 'closed': this.status = 'CLOSED'; break;
			default: this.status = '';
		}
		this.getOrders(true);
	}

	setPage(direction) {

		if (direction === 'next') {
			if (this.page + 1 > this.totalPages) return;
			this.page = this.page + 1;
			this.getOrders();
		}
		if (direction === 'prev') {
			if (this.page - 1 <= 0) return;
			this.page = this.page - 1;
			this.getOrders();
		}
	}

	openDetails($event) {
		if (this.user.role === 'ABSTRACTOR' && $event.selected[0].available === false) return;
		const id = $event.selected[0].id;
		this.router.navigate([`orders/details/${id}/overview`]);
	}

	onSort($event) {
		this.sort = $event.column.prop + ',' + $event.newValue;
		this.getOrders(true);
	}

	getRowClass(row) {
		return {
		  'disabled': row.available === false
		};
	  }
}
