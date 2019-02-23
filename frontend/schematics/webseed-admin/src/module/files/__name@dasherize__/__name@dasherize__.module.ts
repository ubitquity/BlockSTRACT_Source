import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import { ListComponent } from './list/list.component';
import {RouterModule, Routes} from '@angular/router';
import {SharedModule} from '@modules/shared/shared.module';
import {<%= classify(name) %>Service} from './<%= dasherize(name) %>.service';
// import {EditPageComponent} from './edit-page/edit-page.component';
// import {NeweditComponent} from './newedit/newedit.component';

const routes: Routes = [
	{path: '', redirectTo: 'list'},
	{ path: 'list', component: ListComponent },
	// { path: 'create', component: EditPageComponent },
	// { path: 'edit/:id', component: EditPageComponent }
];

@NgModule({
	imports: [
		CommonModule,
		SharedModule,
		RouterModule.forChild(routes)
	],
	exports: [RouterModule],
	// declarations: [ListComponent, EditPageComponent, NeweditComponent],
	declarations: [ListComponent],
	providers: [<%= classify(name) %>Service]
})
export class <%= classify(name) %>Module {}
