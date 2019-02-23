import {Pipe, PipeTransform} from '@angular/core';
import * as _ from 'lodash';

@Pipe({
	name: 'localCurrency'
})
export class LocalCurrencyPipe implements PipeTransform {

	transform(value: any, withoutUnit?: boolean): any {
		if (!_.isNumber(value)) return 'NaN';
		return (value / 100) + (withoutUnit ? '' : 'zł');
	}
}
