import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Logger } from '@wuja/logger';

@Component( {
	selector: 'app-interceptor',
	templateUrl: './interceptor.component.html'
} )
export class InterceptorComponent implements OnInit {

	constructor(private http: HttpClient, private log: Logger) {}

	ngOnInit() {}

	makeCall1 () {
		this.http.get('/someTest1')
			.subscribe(res => {
				this.log.log('Call #1', res);
			});
	}

}
