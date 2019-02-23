import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {RegisterService} from './register.service';
import {Logger} from '@wuja/logger';
import {LoginUtils} from '../../../_services/loginutils/loginutils.service';

import {ToastrService} from 'ngx-toastr';
import {TranslateService} from '@ngx-translate/core';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import {environment} from '../../../../environments/environment';
import { UtilsService } from '../../../_services/utils/utils.service';
import * as _ from 'lodash';

@Component({
	selector: 'app-register',
	templateUrl: './register.component.html',
	styleUrls: ['./register.component.scss']
})
@AutoUnsubscribe()
@LogInit()
export class RegisterComponent implements OnInit {
	registerForm: FormGroup;
	registerError;
	public langs = environment.availableLangs;
	public localName = environment.localName;
	public get currentLang () {
		return this.translate.currentLang;
	}

	public submitLogin$;

	public serverErrors;

	constructor(private fb: FormBuilder,
				private service: RegisterService,
				private log: Logger,
				private loginutils: LoginUtils,
				private toastr: ToastrService,
				private translate: TranslateService,
				private route: ActivatedRoute,
				private utils: UtilsService,
				private router: Router) {

		this.registerForm = this.fb.group({
			// username: [null, Validators.required],
			password: [null, [Validators.required, Validators.maxLength(50), Validators.minLength(4), this.utils.noWhitespaceValidator]],
			confirmPassword: [null, [Validators.required, this.utils.matchOtherValidator('password')]],
			email: [null, [Validators.required, Validators.email]],
			firstName: [null, [Validators.required, Validators.maxLength(100), this.utils.noWhitespaceValidator]],
			lastName: [null, [Validators.required, Validators.maxLength(100), this.utils.noWhitespaceValidator]],
			companyName: [null, [Validators.required, Validators.maxLength(255), this.utils.noWhitespaceValidator]],
			phoneNumber: [null, [Validators.required, Validators.maxLength(20), this.utils.noWhitespaceValidator]],
			weeklyHoursAvailability: [null, [Validators.required, Validators.max(100), Validators.min(0)]],
		});

	}

	ngOnInit() {
		this.log.init('Pages / RegisterComponent', this);
		if (this.loginutils.isLogged) this.router.navigate(['/orders']);
	}


	submit($event) {
		$event.preventDefault();
		const data = _.mapValues(this.registerForm.value, function (element) { return typeof element === 'string' ? element.trim() : element; });
		this.service.register(data)
			.subscribe(res => {
				this.toastr.success('Successfully created Abstractor account. Please check your email to activate account.');
				this.router.navigate(['/login']);
			}, err => {
				this.toastr.error('Error. Please check your form data.');
				this.serverErrors = err.error.fieldErrors;
			});
	}
}
