import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProfileService } from '../profile.service';
import { Logger } from '@wuja/logger';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { AutoUnsubscribe } from '../../../_decorators/autounsub';
import { LogInit } from '../../../_decorators/loginit';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { LoginUtils } from '../../../_services/loginutils/loginutils.service';
import { faPlusCircle, faMinusCircle } from '@fortawesome/free-solid-svg-icons';

@Component({
	selector: 'app-abstract-rates-modal',
	templateUrl: './abstract-rates-modal.component.html',
	styleUrls: ['./abstract-rates-modal.component.scss']
})
@AutoUnsubscribe()
@LogInit()
export class AbstractRatesModalComponent implements OnInit {
	public workingId = null;
	public form: FormGroup;
	public errors;
	public user;
	public abstractRates = [{
		serviceType: {id: null, name: ''},
		name: '',
		rate: null,
		serviceTypeId: null
	}];
	public faPlusCircle = faPlusCircle;
	public faMinusCircle = faMinusCircle;
	public services;
	public initialServices;
	public showForm;

	@Output()
	finished: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor(
		private service: ProfileService,
		private log: Logger,
		private toastr: ToastrService,
		public modal: NgbActiveModal,
		private loginutils: LoginUtils,
		private translate: TranslateService,
		private fb: FormBuilder
	) {}

	ngOnInit() {

		this.user = this.loginutils.user;
		this.form = this.fb.group({
			firstName: [null, Validators.required],
			lastName: [null, Validators.required],
			bankAccount: [null, Validators.required],
			email: [null, [Validators.required, Validators.email]],
			countries: [[], Validators.required],
			countyIds: [[], Validators.required],
			notifications: [null, Validators.required],
			companyName: [null, Validators.required],
			weeklyAvailability: [
				null,
				[
					Validators.required,
					Validators.max(100),
					Validators.min(0)
				]
			],
			abstractRates: []
		});
		this.getAbstractor();
		this.getServices();
	}

	getAbstractor() {
		this.service.getAbstractorProfile().subscribe(res => {
			res.countyIds = res.counties.map(county => county.id);
			this.form.patchValue(res);
			this.showForm = true;
			if (res.abstractRates.length) this.abstractRates = res.abstractRates;
		});
	}

	getServices() {
		this.service.getServices().subscribe(res => {
			this.initialServices = res.list;
			this.checkServices();
		});
	}

	checkServices() {
		this.services = [...this.initialServices];
		this.initialServices.forEach(service => {
			if (this.abstractRates && this.abstractRates.some(rate => service.id === rate.serviceType.id)) {
				this.services.splice(this.services.indexOf(service), 1);
			}
		});
	}

	addRate(rate) {
		this.abstractRates.splice(this.abstractRates.indexOf(rate) + 1, 0, {serviceType: {id: null, name: ''}, name: '', rate: null, serviceTypeId: null});
		this.checkServices();
	}

	deleteRate(absRate) {
		if (this.abstractRates.length === 1) return;
		this.abstractRates.splice(this.abstractRates.indexOf(absRate), 1);
		this.checkServices();
	}

	submit() {
		const data = this.form.value;
		this.abstractRates = this.abstractRates.filter(rate => rate.rate);
		data.abstractRates = this.abstractRates.map(absRate => ( Object.assign(absRate, {
					rate: absRate.rate,
					serviceTypeId: absRate.serviceType.id
				})
		));
		this.log.log('data', data);
		this.service.saveAbstractorProfile(data).subscribe(
			res => {
				this.toastr.success('Abstract Rates successfully saved');
				this.modal.close();
			},
			e => {
				this.log.error('submit', e);
				this.errors = e.error.fieldErrors;
				if (e.error._code === 460) {
					this.toastr.error('Invalid maximum value. Maximum value is 99999999.');
				} else {
					this.toastr.error('Error during saving rates. Please check your data or try again later.');
				}

			}
		);
	}
}
