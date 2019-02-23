import { Injectable } from '@angular/core';
import { HttpAuth } from '../httpauth/http-auth.service';

@Injectable()
export class NotificationsService {

	constructor(private http: HttpAuth) {}

	getNotifications() {
		return this.http.get('/notifications');
	}

	notificationSeen(notificationId) {
		return this.http.put(`/notifications/set-as-seen-up-to/${notificationId}`);
	}

}
