import { Component, OnInit, Input, HostListener, Output, EventEmitter } from '@angular/core';
import { Logger } from '@wuja/logger';
import {AutoUnsubscribe} from '../../../../_decorators/autounsub';
import {LogInit} from '../../../../_decorators/loginit';
import {faAngleLeft, faPlusCircle, faMinusCircle, faCalendarAlt} from '@fortawesome/free-solid-svg-icons';
import { UtilsService } from '../../../../_services/utils/utils.service';
import { ActivatedRoute } from '@angular/router';
import { OrdersService } from '../../orders.service';
import { NgbModal, NgbDateParserFormatter } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmModalComponent } from '../../../../_components/confirm-modal/confirm-modal.component';
import { LoginUtils } from '../../../../_services/loginutils/loginutils.service';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { NgbDateCustomParserFormatter} from './dateformat';



@Component( {
	selector: 'app-fulfillment',
	templateUrl: './fulfillment.component.html',
	styleUrls: ['./fullfillment.component.scss'],
	providers: [
		{provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter}
	   ]
} )

@AutoUnsubscribe()
@LogInit()
export class FulfillmentComponent implements OnInit {

	public fullfillmentForm: FormGroup;

	public faAngleLeft = faAngleLeft;
	public faPlusCircle = faPlusCircle;
	public faMinusCircle = faMinusCircle;
	public faCalendarAlt = faCalendarAlt;
	public id;
	public prevUrl;
	public datepicker = false;
	public searchDate;
	public user;
	// public deeds;
	public titleSearchDocument;
	public ready = false;
	@Input() order;
	@Output() refresh = new EventEmitter<any>();
	@Output() changeTab = new EventEmitter<any>();

	abstractRates;
	initialRates;
	copyCost = 0;
	copyUnits = 0;
	totalCost = 0;
	disableEdit = false;
	disabled = false;
	waitForApprovalDeeds = false;
	waitForApprovalTitle = false;
	titleFile;

	public serverErrors;

	public charges = [
		{
			id: 0,
			orderAbstractRateId: null,
			serviceType: {
				id: null,
				name: 'Choose charge type'
			},
			rate: null,
			units: null
		}
	];

	public parcelIds = [
		{
			id: 1,
			name: ''
		}
	];

	public deeds = [
		{
			id: 0,
			grantor: null,
			grantee: null,
			documentDate: null,
			recordedDate: null,
			recordingBook: null,
			recordingPage: null
		}
	];


	// @HostListener('document:click', ['$event']) clickedOutside($event) {
	// 	this.datepicker = false;
	// }

	// clickedInside($event) {
	// 	$event.preventDefault();
	// 	$event.stopPropagation();
	// 	if ($event.target.id === 'dpBtn' || $event.target.closest('.btn') && $event.target.closest('.btn').id === 'dpBtn') {
	// 		this.datepicker = !this.datepicker;
	// 	} else {
	// 		this.datepicker = true;

	// 	}
	// }

	constructor(private log: Logger,
		private utils: UtilsService,
		private route: ActivatedRoute,
		private loginutils: LoginUtils,
		private modalService: NgbModal,
		private toastr: ToastrService,
		private fb: FormBuilder,
		private service: OrdersService) {
		this.id = this.route.snapshot.params.id;
}

	ngOnInit() {
		this.prevUrl = this.utils.getPreviousUrl();
		this.user = this.loginutils.user;
		this.fullfillmentForm = this.fb.group({
			searchDate: [null, Validators.required],
			titleVesting: [null, [Validators.required, this.utils.noWhitespaceValidator]],
			legalDescription: [null, [Validators.required, this.utils.noWhitespaceValidator]],
			estateType: [null, [Validators.required, this.utils.noWhitespaceValidator]],
			outstandingMortgage: [null, [Validators.required, this.utils.noWhitespaceValidator]],
			commitmentRequirements: [null, [Validators.required, this.utils.noWhitespaceValidator]],
			commitmentExceptions: [null, [Validators.required, this.utils.noWhitespaceValidator]],
			copyCostPerUnit: [null, [this.utils.noWhitespaceValidator, this.utils.zeroValidator]],
			copyUnits: [null, [this.utils.zeroValidator, this.utils.noWhitespaceValidator]],
			parcelIds: [[], Validators.required],
			charges: [[], Validators.required],
			deeds: [[], Validators.required],
		});


		if (this.user.role !== 'ADMIN' && this.order.internalStatus === 'UNCLAIMED') {
			this.changeTab.emit('overview');
			return;
		}
		this.disableEdit = this.user.role !== 'ABSTRACTOR' || this.order.internalStatus === 'PENDING_APPROVAL' || this.order.internalStatus === 'ACCEPTED' || this.order.internalStatus === 'PAID';
		this.getFullfillmentData();
	}

	getFullfillmentData() {
		this.service.getFullfillment(this.id).subscribe(res => {
			this.fullfillmentForm.patchValue(res);
			this.initialRates = res.abstractRates;
			if (res.charges.length) {
				this.manageCharges(res.charges);
				this.disabled = false;
			} else {
				this.checkCharges();
			}
			this.parcelIds = res.parcelIds.length ? res.parcelIds.map(parcelId => parcelId = {'name': parcelId}) : this.parcelIds;
			// this.deeds = res.deedFiles;
			this.titleSearchDocument = res.titleSearchDocumentFile;
			this.log.log('res', res, this.disableEdit);
			if (res.startDate) {
				const from = new Date(res.startDate);
				this.searchDate = `${(from.getMonth() + 1).toString().padStart(2, '0')}/${from.getDate().toString().padStart(2, '0')}/${from.getFullYear()}`;
			}
			this.ready = true;
			this.getTotalCost();
		}, err => {
			this.ready = false;
		}
		);
	}

	fileListener($event) {
		this.titleFile = $event;
		this.waitForApprovalTitle = true;
	}

	saveFiles() {
		this.service.titleSearchDocumentFile(this.id, this.titleFile).subscribe(res => {
			this.titleSearchDocument = res;
			this.waitForApprovalTitle = false;
			this.titleFile = null;
		});
	}

	deleteFile (file = null, $event) {
		$event.preventDefault();
		const instance = this.modalService.open(ConfirmModalComponent, {windowClass: 'transparent'});
		instance.componentInstance.text = 'Are you sure you want to delete this file?';
		instance.componentInstance.titleText = 'Delete File';
		instance.result.then(ok => {
			this.service.deleteTitleFile(this.id).subscribe(res => {
					this.titleSearchDocument = null;
				});
		}, close => {
			return;
		});
	}

	checkCharges() {
		this.abstractRates = [...this.initialRates];
		this.initialRates.forEach(rate => {
			if (this.charges.some(charge => rate.serviceType.id === charge.serviceType.id)) {
				this.abstractRates.splice(this.abstractRates.indexOf(rate), 1);
			}
		});
	}


	manageCharges(charges) {
		this.initialRates.map(rate => charges.some(charge => rate.serviceType.id === charge.orderAbstractRateId ? Object.assign(charge, rate) : null));
		charges.map(charge => charge.id = charges.indexOf(charge));
		this.charges = charges;
		this.checkCharges();
	}

	addCharge(chargeId) {
		this.charges.splice(chargeId + 1, 0, {id: chargeId + 1, serviceType: {id: 0, name: ''}, rate: null, units: null, orderAbstractRateId: null});
		this.charges.map(charge => charge.id = this.charges.indexOf(charge));
		this.disabled = true;
		this.checkCharges();
	}

	deleteCharge(charge) {
		if (this.charges.length === 1) return;
		this.charges.splice(this.charges.indexOf(charge), 1);
		this.charges.map(chargeId => chargeId.id = this.charges.indexOf(chargeId));
		this.charges.some(chargeId => !chargeId.rate ? this.disabled = true :  this.disabled = false);
		this.getTotalCost();
		this.checkCharges();
	}

	addParcelId(parcel) {
		this.parcelIds.splice(this.parcelIds.indexOf(parcel) + 1, 0, {id: this.parcelIds.indexOf(parcel) + 1, name: ''});
		this.parcelIds.map(parcelId => parcelId.id = this.parcelIds.indexOf(parcelId));
	}

	deleteParcelId(parcel) {
		if (this.parcelIds.length === 1) return;
		this.parcelIds.splice(this.parcelIds.indexOf(parcel), 1);
		this.parcelIds.map(parcelId => parcelId.id = this.parcelIds.indexOf(parcelId) + 1);
	}

	addDeed(deed) {
		this.deeds.splice(this.deeds.indexOf(deed) + 1, 0, {
			id: this.deeds.indexOf(deed) + 1,
			grantor: null,
			grantee: null,
			documentDate: null,
			recordedDate: null,
			recordingBook: null,
			recordingPage: null
		});
		this.deeds.map(deedId => deedId.id = this.deeds.indexOf(deedId));
		this.log.log('deed added', this.deeds, deed);
	}

	deleteDeed(deed) {
		if (this.deeds.length === 1) return;
		this.deeds.splice(this.deeds.indexOf(deed), 1);
		this.deeds.map(deedId => deedId.id = this.deeds.indexOf(deedId) + 1);
		this.log.log('deed deleted', this.deeds);
	}

	getTotalCost() {
		const controls = this.fullfillmentForm.controls;
		if (this.charges.some(charge => typeof charge.units === 'number' && charge.units <= 0)
		|| controls.copyCostPerUnit.errors && controls.copyCostPerUnit.errors.zeroValue
		|| controls.copyUnits.errors && controls.copyUnits.errors.zeroValue) {
			this.disabled = true;
		} else {
			this.disabled = false;
		}
		function getSum(total, num) {
			return total + num;
		}
		const formControls = this.fullfillmentForm.controls;
		const totalCharges = this.charges.map(charge => charge.rate * charge.units).reduce(getSum);
		this.totalCost = totalCharges + (formControls['copyCostPerUnit'].value * formControls['copyUnits'].value);
	}

	changeCharge() {
		this.charges.map(charge => {
			charge.id = this.charges.indexOf(charge),
			charge.units = 1;
		});
		this.disabled = false;
		this.getTotalCost();
		this.checkCharges();
	}

	saveOrder(send = false) {
		if (this.disabled) return;
		let newCharges = this.charges.map(charge => Object.assign(charge, {
				'orderAbstractRateId': charge.serviceType.id,
				'units': charge.units
		}));
		const newDeeds = this.deeds.map(deed => Object.assign(deed, {
			documentDate: typeof deed.documentDate === 'object' ? this.utils.datepickerToTimestamp(deed.documentDate) : deed.documentDate,
			recordedDate: typeof deed.recordedDate === 'object' ? this.utils.datepickerToTimestamp(deed.recordedDate) : deed.recordedDate,
		}));

		if (this.charges.some(charge => !charge.rate)) newCharges = null;
		this.fullfillmentForm.patchValue({
			searchDate: typeof this.searchDate === 'object' ? this.utils.datepickerToTimestamp(this.searchDate) : this.searchDate,
			// endDate: typeof this.dateFromForm === 'object' ? this.utils.datepickerToTimestamp(this.dateToForm) : this.dateToForm,
			parcelIds: this.parcelIds.map(id => id.name),
			charges: newCharges,
			deeds: newDeeds
		});

		this.service.putFullfillment(this.id, this.fullfillmentForm.value).subscribe(res => {
			if (send) this.sendOrder();
			if (!send) this.toastr.success('Changes saved');
			window.scroll(0, 0);
			this.getFullfillmentData();
		}, err => {
			this.log.log('err!!', err);
			// this.serverErrors = err.error.fieldErrors;
			if (!send) this.toastr.error('Error while saving form. Please check your form data.');
			if (send) this.toastr.error('Error while sending order for approval. Please check your form data.');
		});
	}


	sendOrderConfirm () {
		const instance = this.modalService.open(ConfirmModalComponent, {windowClass: 'transparent'});
		instance.componentInstance.text = 'Are you sure you want to send this order for approval?';
		instance.componentInstance.titleText = 'Send order for approval';
		instance.result.then(ok => {
			this.saveOrder(true);
		}, close => {
			return;
		});
	}

	sendOrder() {
		this.service.submitFullfillment(this.id).subscribe(res => {
			this.log.log('order sended!', res);
			this.refresh.emit();
			this.disableEdit = true;
			this.getFullfillmentData();
		});
	}



}
