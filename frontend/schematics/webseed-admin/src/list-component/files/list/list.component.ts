import { Component, OnInit} from '@angular/core';
import { LogInit } from '@decorators/loginit';
import { Logger } from '@wuja/logger';
import { Remember } from '@decorators/remember';
import { <%= classify(name) %>Service } from '../<%= dasherize(name) %>.service';

@LogInit()
@Remember()
@Component({
	selector: 'app-list',
	templateUrl: './list.component.html'
})
export class ListComponent implements OnInit {
	public <%= dasherize(name) %>;
	public name = '';
	public type = '';
	public page = 1;
	public size = 5;
	public sort = 'name,desc';

	public options = this.service.options;

	constructor(private log: Logger,
				private service: <%= classify(name) %>Service) {}

	ngOnInit() {
		this.get<%= classify(name) %>();
	}

	public filter (filterObj: {[key: string]: any} = {}): void {
		Object.assign(this, filterObj);
		this.get<%= classify(name) %>();
	}

	private get<%= classify(name) %> () {
		const {page, size, sort, type, name} = this;
		this.service.get<%= classify(name) %>({page, size, sort, type, name})
			.subscribe(res => {
				this.<%= dasherize(name) %> = res;
			});
	}
}
