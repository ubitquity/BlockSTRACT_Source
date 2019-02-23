import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {routes} from '../../routes';
import {PagesModule} from '../../routes/pages/pages.module';

@NgModule({
	imports: [
		CommonModule,
		RouterModule.forRoot(routes),
		PagesModule
	],
	exports: [
		RouterModule
	]
})
export class RoutesModule {
}
