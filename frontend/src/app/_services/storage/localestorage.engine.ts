import {StorageEngine} from './storageengine.interface';
import * as _ from 'lodash';

export class LocalStorageEngine implements StorageEngine {
	prefix: string;

	constructor (prefix = 'LSE') {
		this.prefix = prefix;
		if (!window.localStorage) throw new Error('This device do not provide LocalStorage'); return;
	}
	set(key: string, item) {
		return window.localStorage.setItem(`${this.prefix}-${key}`, JSON.stringify(item));
	}

	get(key: string) {
		return <any>JSON.parse(window.localStorage.getItem(`${this.prefix}-${key}`));
	}

	delete(key: string) {
		return window.localStorage.removeItem(`${this.prefix}-${key}`);
	}

	getAll() {
		const maybeJSON = (jsonString) => {
			try {
				const o = JSON.parse(jsonString);
				if (o && typeof o === 'object') {
					return o;
				}
			} catch (e) { }
			return null;
		};
		const obj = {};
		Object.keys(window.localStorage).filter(item => item.match(this.prefix))
			.forEach(key => obj[key.split(`${this.prefix}-`)[1]] = maybeJSON(window.localStorage.getItem(key)));
		return obj;
	}
}
