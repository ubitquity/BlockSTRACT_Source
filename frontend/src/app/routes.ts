import {Routes} from '@angular/router';
import {LayoutComponent} from './_components/layout/layout.component';
import {LoginComponent} from './routes/pages/login/login.component';
import {IsLoggedGuard} from './_guards/is-logged.guard';
import {Page403Component} from './routes/pages/page403/page403.component';
import {IsAdminGuard} from './_guards/is-admin.guard';
import {ExampleResolver} from './_guards/example.resolver';
import {HasPermsGuard} from './_guards/has-perms.guard';
// import { TemplateAdminModule } from './../../node_modules/@itcraft/template-admin/src/template-admin';
import { RegisterComponent } from './routes/pages/register/register.component';
import { ActivateComponent } from './routes/pages/activate/activate.component';

export const routes: Routes = [
	{path: '', component: LayoutComponent, canActivate: [IsLoggedGuard], children: [
		{ path: '', redirectTo: 'orders', pathMatch: 'full' },
		{ path: 'orders', loadChildren: './../../routes/orders/orders.module#OrdersModule', resolve: {resolvedValue: ExampleResolver} },
		{ path: 'tracker', loadChildren: './../../routes/tracker/tracker.module#TrackerModule', resolve: {resolvedValue: ExampleResolver} },
		{ path: 'reports', loadChildren: './../../routes/reports/reports.module#ReportsModule', resolve: {resolvedValue: ExampleResolver} },
		{ path: 'abstractors', loadChildren: './../../routes/users/users.module#UsersModule'},
		{ path: 'profile', loadChildren: './../../routes/profile/profile.module#ProfileModule' },
		{ path: 'inbox', loadChildren: './../../routes/inbox/inbox.module#InboxModule' },
		/*x*/{ path: 'case', loadChildren: './../../routes/demo/demo.module#DemoModule' },
	]},
	/*x*/{ path: 'theme', loadChildren: './../../_template/template1/template1.module#Template1Module'}, /*\x*/
	{ path: 'login', component: LoginComponent },
	{ path: 'register', component: RegisterComponent },
	{ path: 'activate/:token', component: ActivateComponent },
	{ path: '403', component: Page403Component },
	{ path: 'password/reset/:token', component: LoginComponent },
	{ path: '**', redirectTo: 'orders' }
];
export interface Menus {
	path: string;
	slug: string;
	hidden?: boolean;
	visible?: boolean;
	children?: Menus[];
}
export const menus: Menus[] = [
	{path: '/orders', slug: 'orders'},
	{path: '/reports', slug: 'reports'},
	{path: '/tracker', slug: 'tracker'},
	{path: '/inbox', slug: 'inbox'},
	{path: '/abstractors', slug: 'abstractors', hidden: true},
];
