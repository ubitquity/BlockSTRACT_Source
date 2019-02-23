// tslint:disable
import {environment} from 'environments/environment';


/**
 * @constructor
 * @author wuja
 * @desc ten dekorator to moja osobista świneczka 🐷
 * @param memName unique name
 */
export function Remember(memName?: string) {
	return function ( constructor ) {
		function _tryJSON (json, parse = true){
			try {
				if(parse) {
					const o = JSON.parse(json);
					if (o && typeof o === 'object') return o;
				}
				if (!parse) {
					return JSON.stringify(json)
				}
			}
			catch (e) { }
			return false;
		}
		const original = constructor.prototype.ngOnInit;
		const _storeKey = `${environment.slugName}-${location.pathname}-${constructor.name}`;
		constructor.prototype['__store__'] = Object.create({_storeKey});

		const checkPrimitve = value => typeof value === 'string' || typeof value === 'number' || typeof value === 'boolean';
		const checkObject = value => value && typeof value === 'object' && value.constructor.name === 'Object';
		const saveLocal = (value, store) => {
			value ? _tryJSON(value, false) : true;
			window.localStorage.setItem(_storeKey, JSON.stringify(store));
		};
		let initialKeys = [];

		constructor.prototype.ngOnInit = function () {
			const possibleKeys = Object.keys(this).filter(k => checkPrimitve(this[k]) || checkObject(this[k]));
			initialKeys = possibleKeys;
			const context = this;
			const store = this['__store__'];
			const readLocal = _tryJSON(window.localStorage.getItem(_storeKey)) || {};
			const rebuildValues = keys => keys.forEach(prop => this[prop] = readLocal[prop] || this[prop]);
			const replaceAccessors = keys => {
				keys.forEach(prop => {
					const value = this[prop];
					Object.defineProperty(store, prop, {value, writable: true, enumerable: true});
					Object.defineProperty(context, prop,{
						get() {
							return store[prop]
						},
						set(v) {
							store[prop] = v;
							saveLocal(v, store);
						}
					});
				});
			};

			rebuildValues(possibleKeys);
			replaceAccessors(possibleKeys);

			original && typeof original === 'function' && original.apply(this, arguments);
			window.addEventListener('beforeunload', e => saveLocal(null, store));
		};

		constructor.prototype.ngOnDestroy = function () {
			const possibleKeys = Object.keys(this).filter(k => checkPrimitve(this[k]) || checkObject(this[k]));
			const store = Object.assign({}, ...possibleKeys.filter(key => initialKeys.indexOf(key) > -1).map(key => ({[key]: this[key]})));
			console.warn('save on kill', store);
			saveLocal(null, store);

			original && typeof original === 'function' && original.apply(this, arguments);
		};
	};
}
