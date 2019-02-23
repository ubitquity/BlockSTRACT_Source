import {Injectable} from '@angular/core';
import {LocalStorageEngine} from './localestorage.engine';
import {StorageEngine} from './storageengine.interface';
import {environment} from '../../../environments/environment';
import {defer, Observable, of} from 'rxjs';

@Injectable()
export class Storage {
	private db: StorageEngine;

	constructor() {
		this.db = this.getEngine();
	}

	set(key: string, item: any): Observable<any> {
		return defer(() => of(this.db.set(key, item)));
	}

	get(key: string) {
		return defer(() => of(this.db.get(key)));
	}

	delete(key: string) {
		return defer(() => of(this.db.delete(key)));
	}

	getAll() {
		return defer(() => of(this.db.getAll()));
	}

	private getEngine() {
		// switch (this.settings.app.storagePrefix) {
		//     case 'LocalStorage': return new LocalStorageEngine(this.settings.app.storagePrefix)
		//     // case 'Firebase': return firebase
		//     default: return new LocalStorageEngine(this.settings.app.storagePrefix)
		// }
		// console.warn('conf', this.conf)
		return new LocalStorageEngine(environment.storagePrefix);
	}
}

export const StorageProvider = () => new Storage();

