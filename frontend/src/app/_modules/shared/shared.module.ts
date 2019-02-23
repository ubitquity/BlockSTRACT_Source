import { DropdownMenuComponent } from './../../_components/drop-menu/drop-menu.component';
import { Injector, ModuleWithProviders, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { Logger, LoggerModule } from '@wuja/logger';
import { TranslateModule } from '@ngx-translate/core';

import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { HeaderComponent } from '../../_components/header/header.component';
import { RouterModule } from '@angular/router';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormTextFieldComponent } from '../../_components/form-text-field/form-text-field.component';
import { FormFieldComponent } from '../../_components/form-field/form-field.component';
import { FileUploaderComponent } from '../../_components/file-uploader/file-uploader.component';
import { InlineErrorsComponent } from '../../_components/inline-errors/inline-errors.component';
import { NgSelectModule } from '@ng-select/ng-select';
import { ServerErrorsComponent } from '../../_components/server-errors/server-errors.component';
import { AgmCoreModule } from '@agm/core';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import {ConfirmModalComponent} from '../../_components/confirm-modal/confirm-modal.component';
import {LocaleDatePipe} from '../../_pipes/locale-date.pipe';
import {LocalCurrencyPipe} from '../../_pipes/local-currency.pipe';
import {PagerComponent} from '../../_components/pager/pager.component';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { DatepickerComponent } from '../../_components/datepicker/datepicker.component';
import { ExcerptPipe } from '../../_pipes/excerpt.pipe';
import { OfflineModalComponent } from '../../_components/offline-modal/offline-modal.component';
import { RateModalComponent } from '../../_components/rate-modal/rate-modal.component';
import { NotificationsService } from '../../_services/notifications/notifications.service';


@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		RouterModule,
		ReactiveFormsModule,
		TranslateModule,
		LoggerModule,
		NgbModule,
		NgxDatatableModule,
		NgSelectModule,
		InfiniteScrollModule,
		FontAwesomeModule,
		AgmCoreModule.forRoot({
			apiKey: 'AIzaSyD-79vx4QKpzkhMJvklsT18m5a3JnrcNps'
		})
	],
	entryComponents: [ConfirmModalComponent, OfflineModalComponent, RateModalComponent],
	exports: [
		CommonModule,
		FontAwesomeModule,
		FormsModule,
		ReactiveFormsModule,
		TranslateModule,
		NgbModule,
		NgxDatatableModule,
		ServerErrorsComponent,
		FormTextFieldComponent,
		FormFieldComponent,
		FileUploaderComponent,
		HeaderComponent,
		NgSelectModule,
		AgmCoreModule,
		InfiniteScrollModule,
		DropdownMenuComponent,
		ConfirmModalComponent,
		OfflineModalComponent,
		RateModalComponent,
		LocaleDatePipe,
		LocalCurrencyPipe,
		ExcerptPipe,
		PagerComponent,
		DatepickerComponent
	],
	declarations: [
		ServerErrorsComponent,
		FormTextFieldComponent,
		FormFieldComponent,
		HeaderComponent,
		FileUploaderComponent,
		InlineErrorsComponent,
		DropdownMenuComponent,
		ConfirmModalComponent,
		OfflineModalComponent,
		RateModalComponent,
		LocaleDatePipe,
		LocalCurrencyPipe,
		ExcerptPipe,
		PagerComponent,
		DatepickerComponent
	],
	providers: [
		NotificationsService
	]
})
export class SharedModule {
	static forRoot(): ModuleWithProviders {
		return {
			ngModule: SharedModule
		};
	}
}
