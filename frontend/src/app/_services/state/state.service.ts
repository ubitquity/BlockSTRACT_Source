
import {of as observableOf,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';
import { Logger } from '@wuja/logger';
import * as _ from 'lodash';


import { Storage } from '../storage/storage.service';
//
// export interface State {
// 	[key: string]: any;
// }
// export interface StateStore {
// 	[name: string]: State;
// }

@Injectable()
export class StateService {
	key = `${environment.storageTokenLocation}-store`;
	store = {};

	constructor(private storage: Storage, private log: Logger) {
		this.createTree();
	}

	createTree () {
		this.storage.get(this.key).subscribe(res => {
			this.log.init('State', res);
		});
	}

	save (loc: string, val) {
		const _save = () => _.set(this.store, loc, val);
		if (environment.keepStates) this.storage.set(loc, val).subscribe(res => _save());
		else _save();
	}

	read (loc) {
		const _read = () => _.get(this.store, loc);
		return environment.keepStates ? this.storage.get(loc) : observableOf(_read());
	}
}
