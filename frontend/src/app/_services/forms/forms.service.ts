import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { FormField } from './form-field';

@Injectable()
export class FormsService {
	public toFormGroup( fields: FormField<any>[] ) {
		const group: any = {};

		fields.forEach( field => {
			group[ field.key ] = field.required
				? new FormControl( field.value || '', Validators.required )
				: new FormControl( field.value || '' );
		} );
		return new FormGroup( group );
	}
}
