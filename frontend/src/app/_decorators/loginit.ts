// tslint:disable
import { AppModule } from '../app.module';
import {Logger} from '@wuja/logger';
import {Location} from '@angular/common';

export function LogInit(): any & {log: any} {
	// const L: Logger = AppModule.injector.get(Logger);
	return function ( constructor ) {
		const original = constructor.prototype.ngOnInit;
		// const location: Location = AppModule.injector.get(Location);
		// constructor.prototype.log = constructor.prototype.log || AppModule.injector.get(Logger);
		constructor.prototype.ngOnInit = function () {
			// this.log = this.injector.get(Logger);
			if (this.log) this.log.init(`👾 ${constructor.name}`, this);
			original && typeof original === 'function' && original.apply(this, arguments);
		};
	};
}

// export declare function LogInit<T> (target: T): T & {log: any}
