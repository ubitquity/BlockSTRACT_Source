import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {SettingsService} from '../../_services/settings/settings.service';
import {LoginUtils} from '../../_services/loginutils/loginutils.service';
import {ErrorsService} from '../../_services/errors/errors.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { Interceptor } from '../../_services/interceptor/interceptor';
import { LogoutService } from '../../_services/loginutils/logout.service';
import { FormsService } from '../../_services/forms/forms.service';
import { UtilsService } from '../../_services/utils/utils.service';
import {HttpAuth} from '../../_services/httpauth/http-auth.service';
import {Storage} from '../../_services/storage/storage.service';
import { StateService } from '../../_services/state/state.service';
import {IsLoggedGuard} from '../../_guards/is-logged.guard';
import {IsAdminGuard} from '../../_guards/is-admin.guard';
import {ExampleResolver} from '../../_guards/example.resolver';
import {ReloadUserResolver} from '../../_guards/reload-user.resolver';
import {HasPermsGuard} from '@guards/has-perms.guard';

@NgModule({
	imports: [
		CommonModule,
	],
	providers: [
		Storage,
		LoginUtils,
		LogoutService,
		SettingsService,
		IsLoggedGuard,
		HasPermsGuard,
		IsAdminGuard,
		ErrorsService,
		FormsService,
		UtilsService,
		HttpAuth,
		StateService,
		ExampleResolver,
		ReloadUserResolver,
		{ provide: HTTP_INTERCEPTORS, useClass: Interceptor, multi: true }
	],
	exports: []
})
export class CoreModule {
	static forRoot(): ModuleWithProviders {
		return {
			ngModule: CoreModule
		};
	}
}
