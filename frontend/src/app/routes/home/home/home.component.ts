import {Component, OnInit} from '@angular/core';
import {LoginUtils} from '../../../_services/loginutils/loginutils.service';
import {Logger} from '@wuja/logger';
import { LogoutService } from '../../../_services/loginutils/logout.service';
import {LogInit} from '../../../_decorators/loginit';

@Component({
	selector: 'app-home',
	templateUrl: './home.component.html',
	styleUrls: ['./home.component.scss']
})
@LogInit()
export class HomeComponent {

	constructor(private loginutils: LoginUtils,
				private logoutService: LogoutService,
				private log: Logger) {
	}

	logout() {
		this.logoutService.logout();
	}

}
