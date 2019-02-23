import {Injectable} from '@angular/core';
import {Logger} from '@wuja/logger';
import * as moment from 'moment';

window['moment'] = moment;

@Injectable()
export class IdleService {
	private created = moment();
	timeoutId;
	constructor(private log: Logger) {
		window.addEventListener('mousemove', () => this.resetTimer(), false);
	}

	startTimer () {
		this.timeoutId = window.setTimeout(() => this.inactive, 2000);
	}

	resetTimer () {
		window.clearTimeout(this.timeoutId);
		this.active();
	}

	public inactive () {
		this.log.event('starting idle');
	}
	public active () {
		this.startTimer();
		this.checkDate();
	}

	public checkDate () {
		if (!moment().isSame(this.created, 'day')) location.reload();
	}
}
