import {Component, OnInit} from '@angular/core';

@Component({
	selector: 'app-datepicker-multiple',
	templateUrl: './datepicker3.component.html'
})
export class Datepicker3Component implements OnInit {
	displayMonths = 2;
	navigation = 'select';
	showWeekNumbers = false;

	constructor() {
	}

	ngOnInit() {
	}
}
