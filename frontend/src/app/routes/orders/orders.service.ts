import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {PageOfAnimalDto} from '../../_interfaces/models/PageOfAnimalDto';
import {HttpAuth} from '../../_services/httpauth/http-auth.service';
import { LoginUtils } from '../../_services/loginutils/loginutils.service';

import {shareReplay} from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { Logger } from '@wuja/logger';

@Injectable({
	providedIn: 'root'
})
export class OrdersService {
	constructor (private http: HttpAuth, private translate: TranslateService, private loginutils: LoginUtils, private log: Logger) {}
	public options = {
		sizes: [
			{label: 5, value: 5},
			{label: 10, value: 10},
			{label: 20, value: 20},
			{label: 50, value: 50}
		],
	};

	getOrders(filters = {}): Observable<any> {
		if (filters['page']) filters['page']--;
		if (this.loginutils.user.role === 'ADMIN')	return this.http.get('/orders', filters).pipe(shareReplay());
		if (this.loginutils.user.role === 'ABSTRACTOR')	return this.http.get('/orders/abstractor-list', filters).pipe(shareReplay());
	}



	getOrdersStatuses() {
		return this.http.get(`/orders/orders-in-statuses`);
	}

	getOrderDetails(orderId) {
		return this.http.get(`/order/details/${orderId}`);
	}

	getOrderActivities(orderId) {
		return this.http.get(`/order/details/${orderId}/activities`);
	}

	getOrderFiles(orderId) {
		return this.http.get(`/order/details/${orderId}/files`);
	}

	postFiles(orderId, fileList = null) {
		this.log.log('fileList', fileList);
		const fd = new FormData();
		if (fileList) {
			for (let i = 0; i < fileList.length; i++) {
				fd.append('files', fileList[i]);
			}
		}
		return this.http.post(`/order/details/${orderId}/files`, fd);
	}

	deleteFile(orderId, fileId) {
		return this.http.delete(`/order/details/${orderId}/file/${fileId}`);
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

	acceptOrder(orderId) {
		return this.http.put(`/order/accept/${orderId}`);
	}

	declineOrder(orderId) {
		return this.http.put(`/order/decline/${orderId}`);
	}

	getFullfillment(orderId) {
		return this.http.get(`/order/fulfillment/${orderId}`);
	}

	putFullfillment(orderId, data) {
		return this.http.put(`/order/fulfillment/${orderId}`, data);
	}

	submitFullfillment(orderId) {
		return this.http.put(`/order/fulfillment/${orderId}/submit-for-approval`);
	}

	titleSearchDocumentFile(orderId, fileList) {
		const fd = new FormData();
		if (fileList) {
			for (let i = 0; i < fileList.length; i++) {
				fd.append('titleSearchDocumentFile', fileList[i]);
			}
		}
		return this.http.post(`/order/fulfillment/${orderId}/title-search-document-file`, fd);
	}

	postDeedFiles(orderId, fileList) {
		const fd = new FormData();
		if (fileList) {
			for (let i = 0; i < fileList.length; i++) {
				fd.append('deedFiles', fileList[i]);
			}
		}
		return this.http.post(`/order/fulfillment/${orderId}/deed-files`, fd);
	}

	deleteDeedFile(orderId, deedFileId) {
		return this.http.delete(`/order/fulfillment/${orderId}/deed-file/${deedFileId}`);
	}

	deleteTitleFile(orderId) {
		return this.http.delete(`/order/fulfillment/${orderId}/title-search-document-file`);
	}

	setAsAccepted(orderId, rate) {
		return this.http.put(`/order/fulfillment/${orderId}/set-as-accepted`, rate);
	}

	setAsIncomplete(orderId) {
		return this.http.put(`/order/fulfillment/${orderId}/set-as-incomplete`);
	}

	setAsPaid(orderId) {
		return this.http.put(`/order/fulfillment/${orderId}/set-as-paid`);
	}

	recall(orderId, rate) {
		return this.http.put(`/order/fulfillment/${orderId}/recall`, rate);
	}

	cancel(orderId) {
		return this.http.put(`/order/cancel/${orderId}`);
	}
}
