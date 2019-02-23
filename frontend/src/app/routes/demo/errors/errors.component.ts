import {Component, OnInit} from '@angular/core';
import {Logger} from '@wuja/logger';
import {HttpAuth} from '../../../_services/httpauth/http-auth.service';

@Component({
	selector: 'app-errors',
	templateUrl: './errors.component.html',
	// styleUrls: ['./errors.component.scss']
})
export class ErrorsComponent implements OnInit {
	err;

	constructor(private log: Logger, private http: HttpAuth) {
	}

	ngOnInit() {
		this.log.init('Demo / ErrorsComponent', this);
	}

	getError (code) {
		this.http.get(`/error-code`, { code } )
			.subscribe(res => {
				this.log.log('never gonna happen');
				this.err = 'wait, what ?';
			}, err => {
				this.log.error(`DEMO ${code}`, err);
				this.err = err;
			});
	}

}
