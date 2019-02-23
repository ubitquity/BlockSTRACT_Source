import {Component, OnInit} from '@angular/core';
import {Logger} from '@wuja/logger';

@Component({
	selector: 'app-select',
	templateUrl: './select.component.html'
})
export class SelectComponent implements OnInit {

	constructor(private log: Logger) {}

	ngOnInit() {
		this.log.init('SelectComponent / Demo', this);
	}
}
