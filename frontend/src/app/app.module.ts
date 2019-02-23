import {BrowserModule} from '@angular/platform-browser';
import {Injector, LOCALE_ID, NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {SharedModule} from './_modules/shared/shared.module';
import {LayoutComponent} from './_components/layout/layout.component';
import {RoutesModule} from './_modules/routes/routes.module';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {ToastrModule} from 'ngx-toastr';

import {CoreModule} from './_modules/core/core.module';
import {LoggerModule} from '@wuja/logger';
import {SidebarComponent} from './_components/sidebar/sidebar.component';
import {NgProgressModule} from '@ngx-progressbar/core';
import {NgProgressHttpModule} from '@ngx-progressbar/http';
import { NgProgressHttpConfig } from '@ngx-progressbar/http/lib/ng-progress-http.interface';

export function createTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

const config: NgProgressHttpConfig = {
	silentApis: [
		'http://localhost:4200/appstract-api/notifications',
		'https://lab3.itcraft.pl/appstract-api/notifications',
		'http://localhost:4200/appstract-api/inbox/',
		'https://lab3.itcraft.pl/appstract-api/inbox/',
	]
};

@NgModule({
	declarations: [
		AppComponent,
		LayoutComponent,
		SidebarComponent,
	],
	entryComponents: [

	],
	imports: [
		BrowserModule,
		HttpClientModule,
		SharedModule.forRoot(),
		RoutesModule,
		NgProgressModule.forRoot(),
		NgProgressHttpModule.forRoot(config),
		BrowserAnimationsModule,
		CoreModule.forRoot(),
		ToastrModule.forRoot(),
		NgbModule.forRoot(),
		LoggerModule.forRoot(),
		// StorageModule.forRoot(),
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (createTranslateLoader),
				deps: [HttpClient]
			}
		})
	],
	providers: [
	],
	bootstrap: [AppComponent]
})
export class AppModule {
	static injector: Injector;
	constructor (injector: Injector) {
		AppModule.injector = injector;
	}
}

