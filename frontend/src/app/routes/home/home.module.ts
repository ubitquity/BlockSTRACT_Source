import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import {RouterModule, Routes} from '@angular/router';
import { SharedModule } from '../../_modules/shared/shared.module';

const routes: Routes = [
	{path: '', component: HomeComponent},
];

@NgModule({
	imports: [
		CommonModule,
		SharedModule,
		RouterModule.forChild(routes),
	],
	declarations: [HomeComponent],
	exports: [RouterModule]
})
export class HomeModule { }
