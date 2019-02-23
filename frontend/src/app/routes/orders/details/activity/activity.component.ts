import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Logger } from '@wuja/logger';
import {AutoUnsubscribe} from '../../../../_decorators/autounsub';
import {LogInit} from '../../../../_decorators/loginit';
import {faAngleLeft} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../../_services/utils/utils.service';
import { ActivatedRoute, Router } from '@angular/router';
import { OrdersService } from '../../orders.service';
import { LoginUtils } from '../../../../_services/loginutils/loginutils.service';


@Component( {
	selector: 'app-activity',
	templateUrl: './activity.component.html',
	styleUrls: ['./activity.component.scss']
} )

@AutoUnsubscribe()
@LogInit()
export class ActivityComponent implements OnInit {

	public faAngleLeft = faAngleLeft;
	public id;
	public prevUrl;
	public activities;
	public user;
	@Input() order;
	@Output() refresh = new EventEmitter<any>();
	@Output() changeTab = new EventEmitter<any>();
	showActivites = true;

	constructor(private log: Logger,
		private utils: UtilsService,
		private router: Router,
		private route: ActivatedRoute,
		private loginutils: LoginUtils,
		private service: OrdersService) {
		this.id = this.route.snapshot.params.id;
}

	ngOnInit() {
		this.user = this.loginutils.user;
		if (this.user.role !== 'ADMIN') {
			this.changeTab.emit('overview');
			return;
		}
		this.prevUrl = this.utils.getPreviousUrl();
		this.getActivities();
	}

	getActivities() {
		this.service.getOrderActivities(this.id).subscribe(res => {
			this.activities = res;
			this.showActivites = this.activities.length ? true : false;
		}, err => this.showActivites = false);
	}

	redirect(activity) {
		if (activity.type === 'MESSAGE') {
			this.changeTab.emit('inbox');
		}
	}
}
