import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';
import { RouterModule, Routes } from '@angular/router';
import { UsersService } from './users.service';
import { SharedModule } from '../../_modules/shared/shared.module';
import { ToggleAbstractorComponent } from './toggle-abstractor/toggle-abstractor.component';
import { DetailsComponent } from './details/details.component';

const routes: Routes = [
	{ path: '', redirectTo: 'list' },
	{ path: 'details/:id', component: DetailsComponent },
	{ path: 'list', component: ListComponent }
];

@NgModule( {
	imports: [
		CommonModule,
		SharedModule,
		RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	declarations: [
		ListComponent,
		ToggleAbstractorComponent,
		DetailsComponent
	],
	entryComponents: [
		ToggleAbstractorComponent,
	],
	providers: [ UsersService ]
} )
export class UsersModule {
}
