import {Component} from '@angular/core';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';

const now = new Date();

@Component({
	selector: 'app-datepicker-basic',
	templateUrl: './datepicker1.component.html'
})
export class Datepicker1Component {

	model: NgbDateStruct;
	date: {year: number, month: number};

	selectToday() {
		this.model = {year: now.getFullYear(), month: now.getMonth() + 1, day: now.getDate()};
	}
}
