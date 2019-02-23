import {Injector, ReflectiveInjector} from '@angular/core';
import {Logger} from '@wuja/logger';

export interface PageableObject {
	dpage?: number;
	dsize?: number;
	dupa: Function;
}

export interface Pageable {
	[_: string]: PageableObject | any;
}

export function PageableItems(p?: string[]): ClassDecorator {
	return function (constructor: any) {
		// const i = Injector.create({providers: [Logger]});
		const i = ReflectiveInjector.resolveAndCreate([Logger]);
		const log = i.get(Logger);
		log.log('gra gitara', arguments);
		// todo iterować po elementach p
		constructor.prototype.dupa = () => {log.log('dupa', p); };
	};
}

