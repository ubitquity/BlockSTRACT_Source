
import {map,  debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { ChangeDetectorRef, Component, EventEmitter, OnInit } from '@angular/core';
import { HttpAuth } from '../../../../_services/httpauth/http-auth.service';
import { Logger } from '@wuja/logger';

@Component( {
	selector: 'app-remote-select',
	template: `
		<h5 class="mt-0 mb-1">fetched once</h5>
		<ng-select [items]="animals"
				   bindLabel="name"
				   placeholder="Select animal"
				   [(ngModel)]="animalsSelected">
		</ng-select>
		<h5 class="mt-3 mb-1">fetch on query</h5>
		<ng-select [items]="animalsServerSideFiltered"
				   bindLabel="name"
				   placeholder="Select animals from api"
				   [typeahead]="animalsSearch"
				   [(ngModel)]="animalsServerSideSelected">
		</ng-select>
	`
} )
export class RemoteSelectComponent implements OnInit {
	animals;
	animalsSelected;
	animalsServerSideFiltered;
	animalsServerSideSelected;
	animalsSearch = new EventEmitter<string>();

	constructor(private http: HttpAuth, private log: Logger, private cd: ChangeDetectorRef) {}

	ngOnInit() {
		this.log.init('RemoteSelectComponent / Demo', this);

		this.http.get('/animals')
			.subscribe(res => this.animals = res['content']);

		this.serverSideSearch();
	}

	private serverSideSearch() {
		this.animalsSearch.pipe(
			distinctUntilChanged(),
			debounceTime(200),
			switchMap(term => this.http.get('/animals', {name: term}).pipe(map(res => res['content'])))
		).subscribe(x => {
			this.cd.markForCheck();
			this.animalsServerSideFiltered = x;
		}, (err) => {
			this.animalsServerSideFiltered = [];
		});
	}
}
