import {Pipe, PipeTransform} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {DatePipe} from '@angular/common';

@Pipe({
	name: 'localeDate',
	pure: false
})

export class LocaleDatePipe implements PipeTransform {
	constructor(private ts: TranslateService) {
	}

	transform(value: any, ...args: any[]): any {
		const datePipe: DatePipe = new DatePipe(this.ts.currentLang);
		return datePipe.transform(value, ...args);
	}
}
