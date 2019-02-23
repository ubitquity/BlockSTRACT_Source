import {Component, OnInit} from '@angular/core';

@Component({
	selector: 'app-page403',
	template: `
		<div class="container">
			<div class="d-flex p-3 justify-content-center align-items-center" style="height: 100vh;">
				<div class="flex-column col-sm-8 col-md-7 text-center">
					<h2 class="no-border text-center m-0">{{ 'general.403pageMessage' | translate }}</h2>
					<a routerLink="/">{{ 'general.goBackToHome' | translate }}</a> |
					<a routerLink="/login">{{ 'general.goBackToLogin' | translate }}</a>
				</div>
			</div>
		</div>
	`
})
export class Page403Component implements OnInit {

	constructor() {
	}

	ngOnInit() {
	}

}
