import { Component, OnInit} from '@angular/core';
import { LogInit } from '@decorators/loginit';
import {ReportsService} from '../reports.service';
import {Logger} from '@wuja/logger';
import {faSearch, faEllipsisH, faAngleLeft, faAngleRight} from '@fortawesome/free-solid-svg-icons';
import { TranslateService } from '@ngx-translate/core';
import { TableColumn } from '@swimlane/ngx-datatable';
import { UtilsService } from '../../../_services/utils/utils.service';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';
import { Router } from '@angular/router';

const now = new Date();

@LogInit()
// @Remember()
@Component({
	selector: 'app-list',
	templateUrl: './list.component.html'
})
export class ListComponent implements OnInit {
	faSearch = faSearch;
	faEllipsisH = faEllipsisH;
	faAngleLeft = faAngleLeft;
	faAngleRight = faAngleRight;
	public user;
	public prevUrl;
	public cost = 0;
	public month = 1;

	mostExpensive;
	mostResponsive;
	cheapest;

	won;
	declined;
	recalled;
	totalIncome;

	options = [
	{
		value: 1,
		label: '1 month'
	},
	{
		value: 3,
		label: '3 months'
	},
	{
		value: 6,
		label: '6 months'
	},
	{
		value: 9,
		label: '9 months'
	},
	{
		value: 12,
		label: '12 months'
	},
	];

	scrollBarHorizontal = (window.innerWidth < 1200);


	dtMessages = {
		emptyMessage: this.translate.instant('datatable.nodata'),
		totalMessage: this.translate.instant('datatable.total'),
		selectedMessage: this.translate.instant('datatable.selected')
	};

	constructor(
		private translate: TranslateService,
		private loginutils: LoginUtils,
		private utils: UtilsService,
		private log: Logger,
		private service: ReportsService) {

		}

	ngOnInit() {
		this.user = this.loginutils.user;
		this.prevUrl = this.utils.getPreviousUrl();

		if (this.user.role === 'ADMIN') {
			this.getAbstractors();
			this.getAverageCost();
		}

		if (this.user.role === 'ABSTRACTOR') {
			this.getReports();
		}

	}

	getAbstractors() {
		this.service.mostExpensiveAbstractor().subscribe(res => {
			this.mostExpensive = res;
		});
		this.service.mostResponsiveAbstractor().subscribe(res => {
			this.mostResponsive = res;
		});
		this.service.cheapestAbstractor().subscribe(res => {
			this.cheapest = res;
		});
	}

	getAverageCost() {
		const date = `${now.getFullYear()}-${(now.getMonth() + 1).toString().padStart(2, '0')}-${now.getDate().toString().padStart(2, '0')}`;
		this.service.averageCost(date, this.month).subscribe(res => {
			this.cost = res.value;
		});
	}

	getReports() {
		this.service.projectsRecalled().subscribe(res => {
			this.recalled = res.value;
		});
		this.service.projectsWon().subscribe(res => {
			this.won = res.value;
		});
		this.service.projectsDeclined().subscribe(res => {
			this.declined = res.value;
		});
		this.service.totalIncome().subscribe(res => {
			this.totalIncome = res.value;
		});
	}


}
