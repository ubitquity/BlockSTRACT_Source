import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { ButtonsComponent } from './buttons/buttons.component';
import { LayoutComponent } from './layout/layout.component';
import { HomeComponent } from './home/home.component';
import { FormsComponent } from './forms/forms.component';
import { TablesComponent } from './tables/tables.component';
import { WidgetsComponent } from './widgets/widgets.component';
import { CardsComponent } from './cards/cards.component';

const routes: Routes = [
	{
		path: '', component: LayoutComponent, children: [
		{ path: '', redirectTo: 'home' },
		{ path: 'home', component: HomeComponent },
		{ path: 'buttons', component: ButtonsComponent },
		{ path: 'forms', component: FormsComponent },
		{ path: 'tables', component: TablesComponent },
		{ path: 'widgets', component: WidgetsComponent },
		{ path: 'cards', component: CardsComponent }
	]
	}
];

@NgModule( {
	imports: [
		CommonModule,
		RouterModule.forChild( routes )
	],
	exports: [ RouterModule ],
	declarations: [
		HomeComponent,
		ButtonsComponent,
		LayoutComponent,
		FormsComponent,
		TablesComponent,
		WidgetsComponent,
		CardsComponent
	]
} )
export class Template1Module {
}
