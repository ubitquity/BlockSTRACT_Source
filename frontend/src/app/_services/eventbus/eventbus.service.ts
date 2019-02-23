import {Injectable} from '@angular/core';
import {Logger} from '@wuja/logger';

@Injectable()
export class Eventbus {
	private readonly subscriptions: Map<any, any> = new Map();

	constructor(
		private log: Logger
	) {}

	register (key: string, observable: any) {
		this.log.subscription(`+ registered: ${key}`, this.listEvents.size);
		this.subscriptions.set(key, observable);
		return this.subscriptions.get(key);
	}

	getEvent (key) {
		return this.subscriptions.get(key);
	}
	unregister (key: string) {
		if (this.getEvent(key)) {
			this.subscriptions.delete(key);
			this.log.subscription(`- unregistered: ${key}`, this.listEvents.size);
		} else {
			this.log.subscription(`x not unregistered: ${key}`, this.listEvents.size);
		}
	}

	unregisterList (keys: string[] = []) {
		if (keys.length) {
			keys.forEach(key => this.unregister(key));
		}
	}

	public get listEvents () {
		return this.subscriptions;
	}
}
