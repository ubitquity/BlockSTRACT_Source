import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginComponent} from './login/login.component';
import {SharedModule} from '../../_modules/shared/shared.module';
import {RouterModule} from '@angular/router';
import {LoginService} from './login/login.service';
import { Page403Component } from './page403/page403.component';
import { RegisterComponent } from './register/register.component';
import { RegisterService } from './register/register.service';
import { ActivateComponent } from './activate/activate.component';
import { ActivateService } from './activate/activate.service';

@NgModule({
	imports: [
		CommonModule,
		SharedModule,
		RouterModule
	],
	declarations: [
		LoginComponent,
		RegisterComponent,
		Page403Component,
		ActivateComponent
	],
	exports: [
		LoginComponent,
		RegisterComponent
	],
	providers: [
		LoginService,
		RegisterService,
		ActivateService
	]
})
export class PagesModule { }
