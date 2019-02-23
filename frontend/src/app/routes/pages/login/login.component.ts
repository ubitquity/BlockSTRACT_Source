import { Component, OnInit, OnDestroy } from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {LoginService} from './login.service';
import {Logger} from '@wuja/logger';
import {LoginUtils} from '../../../_services/loginutils/loginutils.service';

import {ToastrService} from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import {environment} from '../../../../environments/environment';
import { UtilsService } from '../../../_services/utils/utils.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OfflineModalComponent } from '../../../_components/offline-modal/offline-modal.component';

@Component({
	selector: 'app-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
@AutoUnsubscribe()
@LogInit()
export class LoginComponent implements OnInit, OnDestroy {
	loginForm: FormGroup;
	passwordRecoveryForm: FormGroup;
	resetForm: FormGroup;
	pending = false;
	loginError;
	passwordRecovery = false;
	passwordReset = false;
	public langs = environment.availableLangs;
	public localName = environment.localName;
	public get currentLang () {
		return this.translate.currentLang;
	}

	public submitReset$;
	public submitLogin$;
	public submitRecovery$;
	public serverErrors;

	constructor(private fb: FormBuilder,
				private service: LoginService,
				private log: Logger,
				private loginutils: LoginUtils,
				private toastr: ToastrService,
				private modalService: NgbModal,
				private translate: TranslateService,
				private route: ActivatedRoute,
				private utils: UtilsService,
				private router: Router) {

		this.loginForm = this.fb.group({
			email: [null, [Validators.required, Validators.email]],
			password: [null, Validators.required]
		});

		this.passwordRecoveryForm = this.fb.group({
			email: [null, [Validators.required, Validators.email]]
		});

		this.resetForm = this.fb.group({
			newPassword: [null, Validators.required],
			confirmPassword: [null, [Validators.required, this.utils.matchOtherValidator('newPassword')]],
			token: [null, Validators.required]
		});
	}

	ngOnInit() {
		this.log.init('Pages / LoginComponent', this, this.route.snapshot.params);
		const token = this.route.snapshot.params['token'];
		this.checkConnection();
		if (this.route.snapshot.params['token']) this.initReset();
		if (this.loginutils.isLogged) this.router.navigate(['/orders']);
	}

	ngOnDestroy() {

	}

	checkConnection() {
		if (!navigator.onLine) {
			const instance = this.modalService.open(OfflineModalComponent, {windowClass: 'transparent', backdrop: 'static', keyboard: false});
			instance.result.then(
				res => this.checkConnection());
		}
	}

	initReset () {
		this.passwordReset = true;
		this.resetForm.patchValue({
			token: this.route.snapshot.params['token']
		});
	}


	submitReset($event) {
		$event.preventDefault();
		this.pending = true;
		this.submitReset$ = this.service.sendNewPassword(this.resetForm.value)
			.subscribe(res => {
				this.toastr.success(this.translate.instant('general.newPasswordSet'));
				this.resetForm.reset();
				this.pending = false;
				this.passwordReset = false;
			}, err => {

				if (err.error._errorDetails === 'FORBIDDEN: ApiException: Token expired') {
					this.toastr.error(`Token expired. Please sent password recovery request again.`);
				} else {
					this.toastr.error(this.translate.instant('general.newPasswordSetFailure'));
				}

				this.serverErrors = err.error ? err.error.fieldErrors : null;
				this.pending = false;
			});
	}

	submit($event) {
		$event.preventDefault();
		const {email, password} = this.loginForm.value;
		this.pending = true;
		this.submitLogin$ = this.service.login(email, password)
			.subscribe(res => this.loginOk(res), err => this.loginErr(err));
	}

	submitRecovery($event) {
		$event.preventDefault();
		const {email} = this.passwordRecoveryForm.value;
		// this.toastr.warning('there is no recevery service');
		this.submitRecovery$ = this.service.sendRecovery(email)
			.subscribe(res => {
				this.toastr.success(this.translate.instant('general.resetEmailSent'));
				this.passwordRecoveryForm.reset();
				this.passwordRecovery = false;
			}, err => {
				if (err.error._errorDetails === 'RESOURCE_NOT_FOUND: ApiException') {
					this.toastr.error(`No account with this email address found`);
				} else if (err.error._errorDetails === 'BUSINESS_ERROR: NOT_ACTIVATED') {
					this.toastr.error(`Your account is not confirmed. Please check your email and use activation link.`);
				} else if (err.error._errorDetails === 'BUSINESS_ERROR: DISABLED') {
					this.toastr.error(`Your Account is not fully activated. When AppStract will activate your account we will let you know via email.`);
				} else {
					this.toastr.error(err.error.errorDetails, this.translate.instant('general.resetEmailFailure'));
				}
			});
	}

	loginOk(res) {
		this.loginutils.user = res.user;
		this.router.navigate(['/orders']);
	}

	loginErr(err) {

		if (err.error.errorType === 'DISABLED') {
			this.log.log('err', err);
			this.loginError = 'login.accountDisabled';
		} else if (err.error.errorType === 'NOT_ACTIVATED') {
			this.loginError = 'login.notActivated';
		} else {
			this.loginError = 'login.badLoginOrPass';
		}
	}
}
