import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HomeComponent } from './home/home.component';
import { RouterModule, Routes } from '@angular/router';
import { ButtonsComponent } from './buttons/buttons.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { AlertsComponent } from './alerts/alerts.component';
import { DatepickerComponent } from './datepicker/datepicker.component';
import { DatatablesComponent } from './datatables/datatables.component';
import { SharedModule } from '../../_modules/shared/shared.module';
import { InterceptorComponent } from './interceptor/interceptor.component';
import { RouteParamsComponent } from './route-params/route-params.component';
import { StateComponent } from './state/state.component';
import { ErrorsComponent } from './errors/errors.component';
import { Datepicker1Component } from './datepicker/datepicker1/datepicker1.component';
import { Datepicker2Component } from './datepicker/datepicker2/datepicker2.component';
import { Datepicker3Component } from './datepicker/datepicker3/datepicker3.component';
import { Datepicker4Component } from './datepicker/datepicker4/datepicker4.component';
import { Datepicker5Component } from './datepicker/datepicker5/datepicker5.component';
import { Datepicker6Component } from './datepicker/datepicker6/datepicker6.component';
import { Datepicker8Component } from './datepicker/datepicker8/datepicker8.component';
import { SelectComponent } from './select/select.component';
import { SimpleSelectComponent } from './select/simple-select/simple-select.component';
import { RemoteSelectComponent } from './select/remote-select/remote-select.component';
import { FormsComponent } from './forms/forms.component';
import { SingleInputComponent } from './forms/single-input/single-input.component';
import { SingleInput2Component } from './forms/single-input2/single-input.component';
import { SimpleFormComponent } from './forms/simple-form/simple-form.component';
import { SimpleFormShortComponent } from './forms/simple-form-short/simple-form-short.component';
import { NestedFormComponent } from './forms/nested-form/nested-form.component';
import { DynamicValidationComponent } from './forms/dynamic-validation/dynamic-validation.component';
import { MapsComponent } from './maps/maps.component';
import { TranslateComponent } from './translate/translate.component';
import { ModalComponent } from './modal/modal.component';
import { ModalModalComponent } from './modal/modal-modal/modal-modal.component';
import { TooltipComponent } from './tooltip/tooltip.component';
import { InfiniteScrollComponent } from './infinite-scroll/infinite-scroll.component';
import { MapsService } from './maps/maps.service';
import { CountryMapComponent } from './maps/country-map/country-map.component';
import { CityFormComponent } from './maps/city-form/city-form.component';
import {ChangeDetectionComponent} from './change-detection/change-detection.component';
import {CdDefaultComponent} from './change-detection/cd-default.component';
import {CdOnpushComponent} from './change-detection/cd-onpush.component';
import {CdDisabledComponent} from './change-detection/cd-disabled.component';

const routes: Routes = [
	{
		path: '', component: HomeComponent, children: [
			{ path: 'buttons', component: ButtonsComponent },
			{ path: 'alerts', component: AlertsComponent },
			{ path: 'datepicker', component: DatepickerComponent },
			{ path: 'datatables', component: DatatablesComponent },
			{ path: 'interceptor', component: InterceptorComponent },
			{ path: 'state', component: StateComponent },
			{ path: 'error', component: ErrorsComponent },
			{ path: 'forms', component: FormsComponent },
			{ path: 'select', component: SelectComponent },
			{ path: 'maps', component: MapsComponent },
			{ path: 'translate', component: TranslateComponent },
			{ path: 'modal', component: ModalComponent },
			{ path: 'tooltip', component: TooltipComponent },
			{ path: 'change-detection', component: ChangeDetectionComponent },
			{ path: 'infinite', component: InfiniteScrollComponent }
		]
	}
];

@NgModule({
	imports: [
		CommonModule,
		RouterModule.forChild(routes),
		NgbModule,
		FormsModule,
		SharedModule
	],
	exports: [RouterModule],
	entryComponents: [
		ModalModalComponent
	],
	declarations: [
		HomeComponent,
		ButtonsComponent,
		AlertsComponent,
		DatepickerComponent,
		DatatablesComponent,
		InterceptorComponent,
		RouteParamsComponent,
		StateComponent,
		ErrorsComponent,
		Datepicker1Component,
		Datepicker2Component,
		Datepicker3Component,
		Datepicker4Component,
		Datepicker5Component,
		Datepicker6Component,
		Datepicker8Component,
		FormsComponent,
		SingleInputComponent,
		SingleInput2Component,
		SimpleFormComponent,
		SimpleFormShortComponent,
		NestedFormComponent,
		SelectComponent,
		DynamicValidationComponent,
		SimpleSelectComponent,
		RemoteSelectComponent,
		MapsComponent,
		TranslateComponent,
		ModalComponent,
		ModalModalComponent,
		TooltipComponent,
		InfiniteScrollComponent,
		CountryMapComponent,
		CityFormComponent,
		ChangeDetectionComponent,
		CdDefaultComponent,
		CdOnpushComponent,
		CdDisabledComponent
	],
	providers: [
		MapsService
	]
})
export class DemoModule {
}
