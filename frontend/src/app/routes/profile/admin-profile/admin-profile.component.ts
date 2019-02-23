import { Component, OnInit } from '@angular/core';
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
	faCheck
} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../_services/utils/utils.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { NeweditComponent } from '../newedit/newedit.component';

@Component({
	selector: 'app-admin-profile',
	templateUrl: './admin-profile.component.html'
})
@AutoUnsubscribe()
@LogInit()
export class AdminProfileComponent implements OnInit {
	public faAngleLeft = faAngleLeft;
	public faEdit = faEdit;
	public faCheck = faCheck;

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

	constructor(
		private log: Logger,
		private toastr: ToastrService,
		private loginutils: LoginUtils,
		private utils: UtilsService,
		private modalService: NgbModal,
		private translate: TranslateService,
		private service: ProfileService
	) {}

	ngOnInit() {
		this.log.init('ProfileComponent / Profile', this);
		// this.profile = this.service.profile;
		this.getProfile();
		this.prevUrl = this.utils.getPreviousUrl();
	}

	getProfile() {
		this.profile$ = this.service.getProfile().subscribe(res => {
			this.profile = res;
		});
	}

	openEditModal() {
		this.edit = !this.edit;
		const instance = this.modalService.open(NeweditComponent, {
			windowClass: 'transparent'
		});
		instance.result.then(
			res => this.getProfile(),
			close => this.getProfile()
		);
	}

	importFromQualia() {
		this.service.importFromQualia().subscribe(res => {
			this.toastr.success('Successfully imported data from Qualia');
		}, error => {
			this.toastr.error('Error while importing from Qualia');
		});
	}
}
