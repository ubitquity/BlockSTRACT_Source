import { Component, Output, EventEmitter } from '@angular/core';
import {NgbDateStruct, NgbCalendar} from '@ng-bootstrap/ng-bootstrap';

const equals = (one: NgbDateStruct, two: NgbDateStruct) =>
	one && two && two.year === one.year && two.month === one.month && two.day === one.day;

const before = (one: NgbDateStruct, two: NgbDateStruct) =>
	!one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
		? false : one.day < two.day : one.month < two.month : one.year < two.year;

const after = (one: NgbDateStruct, two: NgbDateStruct) =>
	!one || !two ? false : one.year === two.year ? one.month === two.month ? one.day === two.day
		? false : one.day > two.day : one.month > two.month : one.year > two.year;

@Component({
	selector: 'app-datepicker-range',
	templateUrl: './datepicker.component.html',
	styles: [`
    .custom-day {
      text-align: center;
      padding: 0.185rem 0.25rem;
      display: inline-block;
      height: 2rem;
      width: 2rem;
    }
    .custom-day.focused {
      background-color: #e6e6e6;
    }
    .custom-day.range, .custom-day:hover {
      background-color: rgb(2, 117, 216);
      color: white;
    }
    .custom-day.faded {
      background-color: rgba(2, 117, 216, 0.5);
		}
		.muted {
			color: #8f8f8f;
		}
  `]
})
export class DatepickerComponent {

	hoveredDate: NgbDateStruct;

	fromDate: NgbDateStruct;
	toDate: NgbDateStruct;

	@Output() dateFrom = new EventEmitter<any>();
	@Output() dateTo = new EventEmitter<any>();

	constructor(calendar: NgbCalendar) {}

	onDateChange(date: NgbDateStruct) {
		if (!this.fromDate && !this.toDate) {
			this.fromDate = date;
		} else if (this.fromDate && !this.toDate && after(date, this.fromDate)) {
			this.toDate = date;
		} else {
			this.toDate = null;
			this.fromDate = date;
		}
		this.dateFrom.emit(this.fromDate);
		this.dateTo.emit(this.toDate);

	}

	isHovered = date => this.fromDate && !this.toDate && this.hoveredDate && after(date, this.fromDate) && before(date, this.hoveredDate);
	isInside = date => after(date, this.fromDate) && before(date, this.toDate);
	isFrom = date => equals(date, this.fromDate);
	isTo = date => equals(date, this.toDate);
}
