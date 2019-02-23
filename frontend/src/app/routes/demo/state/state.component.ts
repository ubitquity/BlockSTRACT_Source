import { Component, OnInit } from '@angular/core';
import { StateService } from '../../../_services/state/state.service';
import { Logger } from '@wuja/logger';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component( {
	selector: 'app-state',
	templateUrl: './state.component.html',
	styles: []
} )
export class StateComponent implements OnInit {

	person: FormGroup;
	personStateLoc = 'statedemoPerson';

	constructor(private log: Logger, private state: StateService, private fb: FormBuilder) {}

	ngOnInit() {
		this.log.init('StateComponent / Demo', this);

		this.person = this.fb.group({
			name: 'Geralt',
			nick: 'wiechu',
			email: 'wiechu90210@itcraft.pl',
			location: 'Rivia'
		});

		this.person.valueChanges.subscribe(res => {
			this.state.save(this.personStateLoc, res);
		});

		this.state.read(this.personStateLoc)
			.subscribe(res => {
				this.log.log('state from the other site', res);
				if (res) this.person.patchValue(res);
			});
	}
}
