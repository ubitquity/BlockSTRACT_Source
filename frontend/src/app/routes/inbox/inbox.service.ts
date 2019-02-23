import {Injectable} from '@angular/core';
import { Observable, BehaviorSubject } from 'rxjs';
import {HttpAuth} from '../../_services/httpauth/http-auth.service';
import {shareReplay} from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { Logger } from '@wuja/logger';

@Injectable({
	providedIn: 'root'
})
export class InboxService {

	constructor (private http: HttpAuth, private translate: TranslateService, private log: Logger) {}


	getConversations() {
		return this.http.get('/inbox/conversations');
	}

	getMessages(id) {
		return this.http.get(`/inbox/conversation/${id}/messages`);
	}

	sendMessage(orderId, content, fileList = null) {
		const fd = new FormData();
		fd.append(
			'content', content
		);
		if (fileList) {
			for (let i = 0; i < fileList.length; i++) {
				fd.append('attachments', fileList[i]);
			}
		}
		return this.http.post(`/inbox/conversation/${orderId}`, fd);
	}

	getListOfAbstractors() {
		return this.http.get('/orders/list-with-abstractors');
	}

	getActiveOrdersList() {
		return this.http.get('/orders/active');
	}

}
