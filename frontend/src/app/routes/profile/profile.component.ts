import { Component, OnInit } from '@angular/core';
import { Logger } from '@wuja/logger';
import { ProfileService } from './profile.service';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../environments/environment';
import { LoginUtils } from '../../_services/loginutils/loginutils.service';
import { TranslateService } from '@ngx-translate/core';
import { AutoUnsubscribe } from '../../_decorators/autounsub';
import { LogInit } from '../../_decorators/loginit';
import {
	faAngleLeft,
	faEdit,
	faCheck
} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../_services/utils/utils.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
	selector: 'app-profile',
	template: `
		<app-admin-profile *ngIf="user.role === 'ADMIN'"></app-admin-profile>
		<app-abstractor-profile *ngIf="user.role === 'ABSTRACTOR'"></app-abstractor-profile>
	`
})
@AutoUnsubscribe()
@LogInit()
export class ProfileComponent {
	public user;

	constructor(private loginutils: LoginUtils) {
		this.user = this.loginutils.user;
	}
}
