import { Component, OnInit } from '@angular/core';
import {ActivatedRoute, NavigationEnd, NavigationStart, Router} from '@angular/router';
import { Logger } from '@wuja/logger';
import { LoginUtils } from './_services/loginutils/loginutils.service';
import {HttpAuth} from './_services/httpauth/http-auth.service';
import { StateService } from './_services/state/state.service';
import {TranslateService} from '@ngx-translate/core';
import {environment} from '../environments/environment';
import {Storage} from './_services/storage/storage.service';
import { ConnectionService } from 'ng-connection-service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { OfflineModalComponent } from './_components/offline-modal/offline-modal.component';


@Component( {
	selector: 'app-root',
	template: `
		<ng-progress></ng-progress>
		<router-outlet></router-outlet>
	`
} )
export class AppComponent implements OnInit {
	title = 'app';
	appRouter$;
	connected = true;

	constructor( private router: Router,
				private route: ActivatedRoute,
				private log: Logger,
				private loginUtils: LoginUtils,
				private modalService: NgbModal,
				private state: StateService,
				private connectionService: ConnectionService,
				private translate: TranslateService,
				private storage: Storage) {
					this.connectionService.monitor().subscribe(isConnected => {
						if (isConnected) {
						  this.connected = true;
						} else {
						  this.connected = false;
						}
						this.checkConnection();
					  });
				}

	ngOnInit() {

		this.appRouter$ = this.router.events.subscribe( event => {
			if ( event instanceof NavigationEnd ) {
				this.log.separator( event.url );
			}
		} );
		this.log.init('AppComponent', this, this.loginUtils.user);
		this.checkLang();
		this.router.events.subscribe(evt => {
			if (!(evt instanceof NavigationEnd)) {
				return;
			}
			window.scrollTo(0, 0);
		});
	}

	checkLang () {
		this.translate.addLangs(environment.availableLangs);
		this.translate.setDefaultLang( environment.language );
		this.storage.get('lang')
			.subscribe(res => this.loginUtils.useLang(res));
	}

	checkConnection() {
		if (!this.connected) {
			const instance = this.modalService.open(OfflineModalComponent, {windowClass: 'transparent', backdrop: 'static', keyboard: false});
			instance.result.then(
				res => this.checkConnection());
		}
	}
}
