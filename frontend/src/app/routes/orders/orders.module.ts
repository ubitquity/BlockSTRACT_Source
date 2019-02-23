import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ListComponent } from './list/list.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../_modules/shared/shared.module';
import { OrdersService } from './orders.service';
import { DetailsComponent } from './details/details.component';
import { OverviewComponent } from './details/overview/overview.component';
import { FulfillmentComponent } from './details/fulfillment/fulfillment.component';
import { ActivityComponent } from './details/activity/activity.component';
import { FilesComponent } from './details/files/files.component';
import { InboxComponent } from './details/inbox/inbox.component';
import { EmptyRatesComponent } from './details/empty-rates-modal/empty-rates-modal.component';

const routes: Routes = [
	{ path: '', redirectTo: 'list' },
	{ path: 'list', component: ListComponent },
	{ path: 'details/:id', component: DetailsComponent },
	{ path: 'details/:id/:tab', component: DetailsComponent }
];

@NgModule({
	imports: [CommonModule, SharedModule, RouterModule.forChild(routes)],
	exports: [RouterModule],
	declarations: [
		ListComponent,
		DetailsComponent,
		OverviewComponent,
		FulfillmentComponent,
		ActivityComponent,
		FilesComponent,
		InboxComponent,
		EmptyRatesComponent
	],
	entryComponents: [EmptyRatesComponent],
	providers: [OrdersService]
})
export class OrdersModule {}
