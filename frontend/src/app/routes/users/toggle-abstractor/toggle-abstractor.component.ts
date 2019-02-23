import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UsersService } from '../users.service';
import { Logger } from '@wuja/logger';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import {AutoUnsubscribe} from '../../../_decorators/autounsub';
import {LogInit} from '../../../_decorators/loginit';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component( {
	selector: 'app-toggle-abstractor',
	templateUrl: './toggle-abstractor.component.html',
	styleUrls: ['./toggle-abstractor.component.scss']
} )
@AutoUnsubscribe()
@LogInit()
export class ToggleAbstractorComponent implements OnInit {
	public workingId = null;
	public form: FormGroup;
	public errors;
	public user$;

	@Input() set id (id) {
		if (!id) return;
		this.workingId = id;
		this.getUser(id);
	}

	@Output()
	finished: EventEmitter<boolean> = new EventEmitter<boolean>();

	constructor(private service: UsersService,
				private log: Logger,
				private toastr: ToastrService,
				public modal: NgbActiveModal,
				private fb: FormBuilder) {
	}

	ngOnInit() {
		this.form = this.fb.group({
			enabled: [null, [Validators.required]]
		});
	}

	getUser (id) {
		this.user$ = this.service.getUser(id)
			.subscribe(res => {
				this.log.log('user', res);
				this.form.patchValue(res);
				this.workingId = res.id;
			}, e => this.log.error('getUser', e));
	}

	submit () {
		const data = this.form.value;
		this.service.toggleAbstractor(data, this.workingId)
			.subscribe(res => {
				this.finished.next(true);
				this.toastr.success('Changes saved');
				this.modal.close();
			}, e => {
				this.log.error('submit', e);
				this.errors = e.error.fieldErrors;
			});
	}

}
