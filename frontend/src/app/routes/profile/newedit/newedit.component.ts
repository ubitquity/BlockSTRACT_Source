import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProfileService } from '../profile.service';
import { Logger } from '@wuja/logger';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';

@Component( {
	selector: 'app-newedit',
	templateUrl: './newedit.component.html',
	styleUrls: ['./newedit.component.scss']
} )
@AutoUnsubscribe()
@LogInit()
export class NeweditComponent implements OnInit {
	public workingId = null;
	public form: FormGroup;
	public errors;
	public user;
	public counties;

	@Output()
	finished: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor(private service: ProfileService,
				private log: Logger,
				private toastr: ToastrService,
				public modal: NgbActiveModal,
				private loginutils: LoginUtils,
				private translate: TranslateService,
				private fb: FormBuilder) {
	}

	ngOnInit() {
		this.user = this.loginutils.user;
		if (this.user.role === 'ADMIN') {
			this.form = this.fb.group({
				bankAccount: [null, Validators.required],
				email: [null, [Validators.required, Validators.email]],
				notifications: [null, Validators.required],
			});
			this.getAdmin();
		} else {
			this.getCounties();
			this.form = this.fb.group({
				firstName: [null, Validators.required],
				lastName: [null, Validators.required],
				companyName: [null, Validators.required],
				bankAccount: [null, Validators.required],
				email: [null, [Validators.required, Validators.email]],
				countries: [[], Validators.required],
				countyIds: [[], Validators.required],
				notifications: [null, Validators.required],
				weeklyAvailability: [null, [Validators.required, Validators.max(100), Validators.min(0)]],
				abstractRates: []
			});
			this.getAbstractor();
		}
	}

	getAdmin () {
		this.service.getProfile()
			.subscribe(res => {
				this.form.patchValue(res);
			}, e => this.log.error('getUser', e));
	}

	getAbstractor () {
		this.service.getAbstractorProfile().subscribe(res => {
			res.countyIds = res.counties.length ? res.counties.map(county => county.id) : this.counties;
			this.form.patchValue(res);
		});
	}

	getCounties() {
		this.service.getCounties().subscribe(res => {
			this.counties = res.list;
		});
	}

	markDisabled() {
		this.form.controls['email'].setErrors({'incorrect': true});
	}

	submit () {
		if (this.form.invalid) return;
		const data = this.form.value;
		if (this.user.role === 'ADMIN') {
		this.service.updateProfile(data)
			.subscribe(res => {
				this.toastr.success('Account Informations successfully saved');
				this.modal.close();
			}, e => {
				this.log.error('submit', e);
				this.errors = e.error.fieldErrors;
			});
		} else {
			data.abstractRates = data.abstractRates.map(absRate => absRate = {rate: absRate.rate, serviceTypeId: absRate.serviceType.id});
			this.log.log('data', data);
			this.service.saveAbstractorProfile(data)
			.subscribe(res => {
				this.toastr.success('Account Informations successfully saved');
				this.modal.close();
			}, e => {
				this.log.error('submit', e);
				this.errors = e.error.fieldErrors;
			});
		}
	}

}
