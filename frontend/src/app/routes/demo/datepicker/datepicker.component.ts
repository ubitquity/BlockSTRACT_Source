import {Component, Injectable, OnInit} from '@angular/core';
import {NgbCalendar, NgbDateAdapter, NgbDatepickerConfig, NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {Logger} from '@wuja/logger';

@Component({
	selector: 'app-datepicker',
	templateUrl: './datepicker.component.html',
	styleUrls: ['./datepicker.component.scss']

})
export class DatepickerComponent implements OnInit {
	constructor(private log: Logger) {}
	ngOnInit () {
		this.log.init('DatepickerComponent / Demo', this);
	}
}
