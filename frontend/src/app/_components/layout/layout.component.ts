import { Component, OnInit, HostListener, OnDestroy } from '@angular/core';
import {Router} from '@angular/router';
import {Logger} from '@wuja/logger';
import { environment } from '../../../environments/environment';
import { LoginUtils } from '../../_services/loginutils/loginutils.service';
import { UserDto } from '../../_interfaces/models/UserDto';
import { LogoutService } from '../../_services/loginutils/logout.service';
import {VERSION} from '../../../environments/version';
import {faBell, faEnvelopeOpen, faExclamationCircle} from '@fortawesome/free-solid-svg-icons';
import { faTimesCircle, faCheckCircle,  } from '@fortawesome/free-regular-svg-icons';
import { NotificationsService } from '../../_services/notifications/notifications.service';
import { interval } from 'rxjs';


@Component({
	selector: 'app-layout',
	templateUrl: './layout.component.html',
	styleUrls: ['./layout.component.scss']
})
export class LayoutComponent implements OnInit, OnDestroy {
	faBell = faBell;
	faEnvelopeOpen = faEnvelopeOpen;
	faTimesCircle = faTimesCircle;
	faCheckCircle = faCheckCircle;
	faExclamationCircle = faExclamationCircle;
	toggleTop;
	public user;
	public localName = environment.localName;
	public version = VERSION;

	public notifications;
	public intervalToggle;



	constructor(private log: Logger, private loginutils: LoginUtils, public logout: LogoutService, private router: Router, private notificationService: NotificationsService) {
		router.events.subscribe(() => {
			this.toggleTop = false;
		});
		this.intervalToggle = interval(6000).pipe()
		.subscribe(response => this.getNotifications());
	}
	ngOnInit() {
		this.user = this.loginutils.user;
		if (this.user) this.getNotifications();
	}

	ngOnDestroy(): void {
		if (this.intervalToggle) this.intervalToggle.unsubscribe();
	}

	@HostListener('document:click', ['$event']) clickedOutside($event) {
		this.toggleTop = false;
	}

	clickedInside($event: Event) {
		$event.preventDefault();
		$event.stopPropagation();
	  }

	  getNotifications() {
		  this.notificationService.getNotifications().subscribe(res => {
			  this.notifications = res;
			//   this.notifications[0].type = 'ORDER_CANCELLED';
		  });
	  }

	  checkNotification(notification) {
		  this.notificationService.notificationSeen(notification.id).subscribe(res => {
			  this.log.log('seen', notification);
			  this.getNotifications();
		  });
		  if (notification.type === 'ORDER_RECALLED' || notification.type === 'ORDER_CANCELLED') {
			  return;
		  }
		  if (notification.type === 'MESSAGE') {
			  this.router.navigate([`/inbox/chat/${notification.orderId}`]);
		  }
		  if (notification.type !== 'MESSAGE') {
			  this.router.navigate([`/orders/details/${notification.orderId}`]);
		  }
	  }
}
