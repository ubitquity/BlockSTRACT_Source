import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import { ListComponent } from './list/list.component';
import {RouterModule, Routes} from '@angular/router';
import {SharedModule} from '../../_modules/shared/shared.module';
import {TrackerService} from './tracker.service';

const routes: Routes = [
	{path: '', redirectTo: 'list'},
	{ path: 'list', component: ListComponent },
];

@NgModule({
	imports: [
		CommonModule,
		SharedModule,
		RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	declarations: [ListComponent],
	providers: [TrackerService]
})
export class TrackerModule {}
