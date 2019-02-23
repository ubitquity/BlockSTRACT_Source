import {Subject} from 'rxjs';
import {OnDestroy} from '@angular/core';

export class AutoUnsub implements OnDestroy {
	public ngUnsubscribe: Subject<void> = new Subject<void>(); // = new Subject(); in Typescript 2.2-2.4
	eb;
	componentEvents;
	constructor(e$, eb) {
		this.eb = eb;
		this.componentEvents = e$;
	}
	ngOnDestroy() {
		this.ngUnsubscribe.next();
		this.ngUnsubscribe.complete();
		this.eb.unregisterList(this.componentEvents);
	}
}
