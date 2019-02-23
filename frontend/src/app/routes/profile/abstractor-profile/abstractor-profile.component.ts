import { Component, OnInit, Input } from '@angular/core';
import { Logger } from '@wuja/logger';
import { ProfileService } from '../profile.service';
import { ToastrService } from 'ngx-toastr';
import { environment } from '../../../../environments/environment';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';
import { TranslateService } from '@ngx-translate/core';
import { AutoUnsubscribe } from '../../../_decorators/autounsub';
import { LogInit } from '../../../_decorators/loginit';
import {
	faAngleLeft,
	faEdit,
	faCheck,
	faStar
} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../_services/utils/utils.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NeweditComponent } from '../newedit/newedit.component';
import { ActivatedRoute } from '@angular/router';
import { AbstractRatesModalComponent } from './../abstract-rates-modal/abstract-rates-modal.component';

@Component({
	selector: 'app-abstractor-profile',
	templateUrl: './abstractor-profile.component.html'
})
@AutoUnsubscribe()
@LogInit()
export class AbstractorProfileComponent implements OnInit {
	public faAngleLeft = faAngleLeft;
	public faEdit = faEdit;
	public faCheck = faCheck;
	public faStar = faStar;

	public profile;
	public resetFlag = false;
	public file = null;
	public langs = environment.availableLangs;
	public get currentLang() {
		return this.translate.currentLang;
	}

	public profile$;
	public uploadFile$;
	public prevUrl;
	public edit;
	public notifications;

	public user;
	public abstractor;
	public id;

	constructor(
		private log: Logger,
		private toastr: ToastrService,
		private loginutils: LoginUtils,
		private utils: UtilsService,
		private modalService: NgbModal,
		private route: ActivatedRoute,
		private translate: TranslateService,
		private service: ProfileService
	) {}

	ngOnInit() {
		this.log.init('UsersComponent / Details', this);
		this.prevUrl = this.utils.getPreviousUrl();
		this.user = this.loginutils.user;
		this.getUser();
	}

	getUser() {
		this.profile$ = this.service.getAbstractorProfile().subscribe(res => {
			this.abstractor = res;
		});
	}

	openEditModal() {
		const instance = this.modalService.open(NeweditComponent);
		instance.result.then(res => this.getUser(), close => {});
	}

	openAbstractRates() {
		const instance = this.modalService.open(AbstractRatesModalComponent);
		instance.result.then(res => this.getUser(), close => {});
	}
}
