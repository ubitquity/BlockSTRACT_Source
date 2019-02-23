import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
	name: 'excerpt',
	pure: false
})
export class ExcerptPipe implements PipeTransform {
	transform(text: String, length: any): any {
		if (!text || !length) {
			return text;
		}
		if (text.length > length) {
			const array = text.split(' ');
			let dots = '';
			if (array.length > length) dots = '...';
			const string = array.splice(0, length).join(' ');
		  return string + dots;
		}
		return text;
	}
}
