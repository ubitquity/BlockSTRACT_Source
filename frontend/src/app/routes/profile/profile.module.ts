import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminProfileComponent } from './admin-profile/admin-profile.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../_modules/shared/shared.module';
import { ProfileService } from './profile.service';
import { NeweditComponent } from './newedit/newedit.component';
import { AbstractorProfileComponent } from './abstractor-profile/abstractor-profile.component';
import { ProfileComponent } from './profile.component';
import { AbstractRatesModalComponent } from './abstract-rates-modal/abstract-rates-modal.component';

const routes: Routes = [
	{path: '', component: ProfileComponent}
];

@NgModule( {
	imports: [
	CommonModule,
		RouterModule.forChild(routes),
		SharedModule
	],
	providers: [
		ProfileService
	],
	declarations: [
		AdminProfileComponent,
		NeweditComponent,
		AbstractorProfileComponent,
		ProfileComponent,
		AbstractRatesModalComponent
	],
	exports: [
		RouterModule
	],
	entryComponents: [
		NeweditComponent,
		AbstractRatesModalComponent,
	]
} )
export class ProfileModule {
}
