import { Component, OnInit } from '@angular/core';
import { Logger } from '@wuja/logger';
import { ActivatedRoute } from '@angular/router';

@Component( {
	selector: 'app-buttons',
	templateUrl: './buttons.component.html',
	// styleUrls: [ './buttons.component.scss' ]
} )
export class ButtonsComponent implements OnInit {
	modelRadio = 1;
	model = {
		left: true,
		middle: false,
		right: false
	};

	constructor(private log: Logger, private route: ActivatedRoute) {
	}

	ngOnInit() {
		this.log.init('ButtonsComponent / Demo', this);
	}

}
