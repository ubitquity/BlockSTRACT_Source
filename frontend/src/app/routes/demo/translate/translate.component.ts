import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';

@Component( {
	selector: 'app-translate',
	template: `
		<app-header name="TranslateModule" class="no-border"></app-header>
		<div class="card mb-5">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Tłumaczenia na widoku</h3>
				<p>Bezpośrednie użycie w widoku wymaga zastosowania pipe'a: </p>
				<pre class="bg-light p-3" ngNonBindable>{{ some.phrase | translate }}</pre>
			</div>
		</div>

		<div class="card mb-5">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Tłumaczenia w kontrolerze/serwisie</h3>
				<p>Użycie w kontrolerze lub dowolnym miejscu odbywa się na dwa sposoby: </p>
				<pre class="bg-light p-3 mb-3" ngNonBindable>this.translateService.instant('some.phrase')</pre>
				<pre class="bg-light p-3 mb-3" ngNonBindable>this.translateService.get('some.phrase').subscribe()</pre>
			</div>
		</div>

		<div class="card mb-5">
			<div class="card-body">
				<h3 class="mt-0 mb-3">Tłumaczenia ze zmiennymi</h3>
				<pre class="bg-light p-3 mb-3" >{{ 'demo.some' | translate: {value: 'whaaat webseed'} }}</pre>
				<pre class="bg-light p-3 mb-3" ngNonBindable>{{ 'demo.some' | translate: {value: 'whaaat webseed'} }}</pre>
				<!-- https://github.com/ngx-translate/core -->
			</div>
		</div>
	`
} )
export class TranslateComponent implements OnInit {

	constructor(private translate: TranslateService) {
	}

	ngOnInit() {
	}

}
