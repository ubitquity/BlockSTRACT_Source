import {AfterViewInit, Component, OnInit, TemplateRef, ViewChild} from '@angular/core';
import { LogInit } from '@decorators/loginit';
import {TrackerService} from '../tracker.service';
import {Logger} from '@wuja/logger';
import {Remember} from '@decorators/remember';
import {faSearch, faEllipsisH, faAngleLeft, faAngleRight} from '@fortawesome/free-solid-svg-icons';
import { TranslateService } from '@ngx-translate/core';
import { TableColumn } from '@swimlane/ngx-datatable';
import { UtilsService } from '../../../_services/utils/utils.service';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';


@LogInit()
// @Remember()
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
	public status = '';
	public pages = [];
	public size = {label: 10, value: 10};
	public sort = '';
	public totalPages;
	public totalElements;
	public currentTab = 'all';
	public tableColumns: TableColumn[];
	public user;
	public search = '';
	public prevUrl;
	public selected = [];
	public currentSelected;
	public showSearch;
	public filtered;
	@ViewChild('buttonsColumn') buttonsColumn: TemplateRef<any>;
	@ViewChild('statusColumn') statusColumn: TemplateRef<any>;
	@ViewChild('priceColumn') priceColumn: TemplateRef<any>;
	@ViewChild('trackerTable') trackerTable: any;
	@ViewChild('expandColumn') expandColumn: any;

	orders;
	expanded = {};
	showOrders = true;

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
		private loginutils: LoginUtils,
		private service: TrackerService) {
			window.onresize = () => {
				this.scrollBarHorizontal = (window.innerWidth < 1200);
			  };
		}

	ngOnInit() {
		this.getOrders();
		this.user = this.loginutils.user;
		this.prevUrl = this.utils.getPreviousUrl();
	}

	ngAfterViewInit() {
		this.log.log('buttons', this.expandColumn);
		this.tableColumns = this.utils.columnsForDatatables([
			{
				name: '',
				cellTemplate: this.expandColumn,
				maxWidth: 40,
				cellClass: 'edit tracker px-0'
			},
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
			}
		]);
	}


	p ($event) {
		this.log.log('p', $event);
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
		const {sort, status, search} = this;
		let page = this.page;
		if (reset) page = 0;
		const size = this.size.value;
		this.service.getOrders({page, status, search, size, sort})
			.subscribe(res => {
				this.orders = res;
				this.orders.content = res.content.map(element => ({
					...element,
					dueDate: `${new Date(element.dueDate).getMonth() + 1}/${new Date(element.dueDate).getDate()}/${new Date(element.dueDate).getFullYear()}`
				}));
				this.page = res.number + 1;
				this.totalPages = res.totalPages;
				this.pages.length = 0;
				for (let i = 1; i <= res.totalPages; i++) {
					this.pages.push(i);
				}
				this.totalElements = res.totalElements;
				this.showOrders = this.orders.content.length ? true : false;


			}, res => this.showOrders = false);
	}

	changeCurrentTab(target) {
		this.currentTab = target;
		switch (target) {
			case 'all':	this.status = ''; break;
			case 'open': this.status = 'OPEN'; break;
			case 'cancelled': this.status = 'CANCELLED'; break;
			case 'closed': this.status = 'CLOSED'; break;
			default: this.status = '';
		}

		this.getOrders(true);
		this.log.log('target', this.currentTab);
	}

	setPage(direction) {

		if (direction === 'next') {
			if (this.page + 1 > this.totalPages) return;
			this.page = this.page + 1;
			this.log.log(direction, this.page);
			this.getOrders();
		}
		if (direction === 'prev') {
			this.log.log(direction, this.page);
			if (this.page - 1 <= 0) return;
			this.page = this.page - 1;
			this.getOrders();
		}
	}

	toggleExpandRow(row) {
		if (this.currentSelected && row.selected[0].id === this.currentSelected.id) {
			this.trackerTable.rowDetail.toggleExpandRow(this.currentSelected);
		} else {
			this.currentSelected = row.selected[0];
			this.trackerTable.rowDetail.collapseAllRows();
			this.trackerTable.rowDetail.toggleExpandRow(this.currentSelected);
		}
	}

	onSort($event) {
		this.sort = $event.column.prop + ',' + $event.newValue;
		this.getOrders(true);
	}
}
