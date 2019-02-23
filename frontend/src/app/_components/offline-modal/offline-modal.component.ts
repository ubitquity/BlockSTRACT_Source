import {Component, OnInit} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import { ConnectionService } from 'ng-connection-service';

@Component({
	selector: 'app-offline-modal-component',
	template: `<div class="py-4 px-3 text-center card">
		<h4>You are offline</h4>
		<p class="mt-3 mb-4">Please check your Internet connection and try again.</p>
		<div class="d-flex mx-auto">
			<button *ngIf="!loading" class="btn btn-primary btn-lg mr-2" (click)="waitForConnection()">Try again</button>
		</div>
	</div>`,
	styles: [`
		button {
			min-width: 90px;
		}
	`]
})

export class OfflineModalComponent {
	loading = false;
	connected = false;
	constructor(public activeModal: NgbActiveModal, private connectionService: ConnectionService) {
		this.connectionService.monitor().subscribe(isConnected => {
			if (isConnected) {
				this.connected = true;
			  } else {
				this.connected = false;
			  }
			  this.checkConnection();
			});
			const interval = window.setTimeout(() => {
				this.checkConnection();
				window.clearInterval(interval);
			}, 1500);

	}

	waitForConnection() {
		this.loading = true;
		const interval = window.setTimeout(() => {
			this.loading = false;
			this.checkConnection();
			window.clearInterval(interval);
		}, 500);

	}

	checkConnection() {
		if (this.connected) {
			this.activeModal.close();
		}
	}
}
