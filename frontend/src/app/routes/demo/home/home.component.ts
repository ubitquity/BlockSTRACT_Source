import { Component, OnInit } from '@angular/core';

@Component( {
	selector: 'app-home',
	template: `
		<div class="row">
			<aside class="col-sm-3">
				<ul class="list-unstyled">
					<li><a [routerLink]="['./buttons']" routerLinkActive="active">{{'case.buttons' | translate}}</a></li>
					<li><a [routerLink]="['./alerts']" routerLinkActive="active">{{'case.alerts' | translate}}</a></li>
					<li><a [routerLink]="['./datepicker']" routerLinkActive="active">{{'case.datepicker' | translate}}</a></li>
					<li><a [routerLink]="['./datatables']" routerLinkActive="active">{{'case.datatables' | translate}}</a></li>
					<li><a [routerLink]="['./interceptor']" routerLinkActive="active">{{'case.interceptor' | translate}}</a></li>
					<li><a [routerLink]="['./state']" routerLinkActive="active">{{'case.state' | translate}}</a></li>
					<li><a [routerLink]="['./error']" routerLinkActive="active">{{'case.error' | translate}}</a></li>
					<li><a [routerLink]="['./forms']" routerLinkActive="active">{{'case.forms' | translate}}</a></li>
					<li><a [routerLink]="['./select']" routerLinkActive="active">{{'case.select' | translate}}</a></li>
					<li><a [routerLink]="['./maps']" routerLinkActive="active">{{'case.maps' | translate}}</a></li>
					<li><a [routerLink]="['./translate']" routerLinkActive="active">{{'case.translate' | translate}}</a></li>
					<li><a [routerLink]="['./modal']" routerLinkActive="active">{{'case.modal' | translate}}</a></li>
					<li><a [routerLink]="['./tooltip']" routerLinkActive="active">{{'case.tooltip' | translate}}</a></li>
					<li><a [routerLink]="['./infinite']" routerLinkActive="active">{{'case.infiniteScroll' | translate}}</a></li>
					<li><a [routerLink]="['./change-detection']" routerLinkActive="active">{{'case.changeDetection' | translate}}</a></li>
				</ul>
			</aside>
			<main class="col-sm-9">
				<router-outlet></router-outlet>
			</main>
		</div>
	`
} )
export class HomeComponent {}

