import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable()
export class LogoutService {
	public status = new Subject<boolean>();

	logout() {
		this.status.next( true );
	}
}
